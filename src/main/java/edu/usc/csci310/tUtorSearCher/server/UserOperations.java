package edu.usc.csci310.tUtorSearCher.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserOperations {

    @GetMapping(value = "getTutors")
    public String getTutorList() {
        return "tutors!";
    }
}
