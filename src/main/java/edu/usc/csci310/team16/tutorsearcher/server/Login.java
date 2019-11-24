package edu.usc.csci310.team16.tutorsearcher.server;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.UserProfile;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.CourseDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.RequestDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.AuthTokenService;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.NotificationService;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Login {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private NotificationService notificationService;


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
        Long id = null;
        try {
            id = userService.addUser(email, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (id == null) { // handles email already registered
            resBody.put("err", "Email already registered");
            return resBody;
        }
        resBody.put("success", true);
        resBody.put("id", id);
        String token = authTokenService.generateNewToken(id);
        res.addHeader("access-token", token);
        return resBody;
    }

    @PostMapping(value = "signin")
    public Object login(HttpServletResponse res, @RequestBody Map<String, String> json) {
        String email = json.get("email");
        String password = json.get("password");
        User user = userService.findUserByCredentials(email, password);
        if (user == null) {
            return "{}";
        }
        String token = authTokenService.generateNewToken(user.getId());
        res.addHeader("access-token", token);
        return new UserProfile(user);
    }

    @PostMapping(value = "validateToken",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Object validate(@RequestParam(value="id") Integer id, @RequestParam(value="token") String token) {
        if (!authTokenService.validateUserToken(id, token)) {
            return "{}";
        }
        return new UserProfile(userService.findUserById(id));
    }

    @GetMapping(value = "test")
    public Object test(@RequestBody UserProfile profile) {
        int count = notificationService.getUnpushedNotificationCount(profile.getId());

        return null;
    }
}
