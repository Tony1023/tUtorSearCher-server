package edu.usc.csci310.team16.tutorsearcher.server;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserOperations {

    private UserDAO dao = new UserDAO();

    @PostMapping(value = "updateProfileImage",
            consumes = MediaType.IMAGE_JPEG_VALUE
    )
    public String receiveImage(MultipartFile image) {

        return "placeholder";
    }

    @PostMapping(value = "cancelImageUpdate")
    public String cancelUpload() {
        return "placeholder";
    }

    @PostMapping(value = "updateProfile")
    public String updateProfile(@RequestBody Map<String, String> json) {
        return "placeholder";
    }

    @GetMapping(value = "searchTutor")
    public List<UserProfile> searchTutor(@RequestBody Map<String, Object> json) {
        return null;
    }

    @PostMapping(value = "sendRequest")
    public String sendRequest(@RequestBody Map<String, Object> json) {
        
        return "success";
    }

    @GetMapping(value = "getNotifications")
    public Object getNotifications() {

        return null;
    }

    @PostMapping(value = "acceptRequest")
    public String acceptRequest() {
        return "success";
    }

    @PostMapping(value = "rejectRequest")
    public String rejectRequest() {
        return "success";
    }

    @GetMapping(value = "getTutors")
    public Object getTutors() {
        return "tutors!";
    }

    @GetMapping(value = "getRating")
    public Double getRating(@RequestBody Map<String, Integer> json) {
        Integer tutorId = json.get("tutor_id");
        Integer tuteeId = json.get("tutee_id");
        return dao.getRating(tutorId, tuteeId); // TODO: verify that returning null works
    }

    @PostMapping(value = "rateTutor")
    public String rateTutor() {

        return "success";
    }



}
