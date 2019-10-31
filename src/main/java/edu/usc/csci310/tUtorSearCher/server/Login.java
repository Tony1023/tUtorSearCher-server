package edu.usc.csci310.tUtorSearCher.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class Login {

    @PostMapping(value = "signup")
    public Object register(HttpServletResponse res) {
        res.addHeader("access-token", "112233");
        return 1023;
    }

    @PostMapping(value = "signin")
    public String login() {
        return "logged in";
    }
}
