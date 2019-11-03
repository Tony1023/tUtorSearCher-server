package edu.usc.csci310.team16.tutorsearcher.server;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Login {

    private UserDAO dao = new UserDAO();

    @GetMapping(value = "/")
    public String landing() {
        return "Hello world";
    }

    @PostMapping(value = "signup")
    public Object register(HttpServletResponse res, @RequestBody Map<String, String> json) {
        String email = json.get("email");
        String password = json.get("password");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("success", false);
        if (email.isEmpty() || password.isEmpty()) {
            resBody.put("err", "Email and password cannot be empty");
            return resBody;
        }
        if (!email.endsWith("@usc.edu")) {
            resBody.put("err", "Must be a usc email");
            return resBody;
        }
        Integer id = dao.registerUser(email, password);
        if (id == null) { // handles email already registered
            resBody.put("err", "Email already registered");
            return resBody;
        }
        resBody.put("success", true);
        resBody.put("id", id);
        String token = dao.getToken(id);
        res.addHeader("access-token", token);
        return resBody;
    }

    @PostMapping(value = "signin")
    public Object login(HttpServletResponse res, @RequestBody Map<String, String> json) {
        String email = json.get("email");
        String password = json.get("password");
        UserProfile user = dao.findUserByCredentials(email, password);
        if (user == null) {
            return "{}";
        }
        String token = dao.getToken(user.getId());
        res.addHeader("access-token", token);
        return user;
    }

    @PostMapping(value = "validateToken",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Object validate(@RequestParam(value="id") Integer id, @RequestParam(value="token") String token) {
        if (!dao.validateUserToken(id, token)) {
            return "{}";
        }
        return dao.findUserById(id);
    }
}
