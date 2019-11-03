package edu.usc.csci310.team16.tutorsearcher.server;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/user")
public class UserOperations {

    private UserDAO dao = new UserDAO();

    @PostMapping(value = "updateProfileImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String receiveImage(HttpServletRequest req, MultipartFile image) {
        try {
            File file = new File("/Users/TonyLyu", "upload.png");
            // TODO: get a tomcat managed path
            if (file.createNewFile()) {
                image.transferTo(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "placeholder";
    }

    @PostMapping(value = "cancelImageUpdate")
    public String cancelUpload(HttpServletRequest req) {
        // Delete id-temp.jpg file
        return "placeholder";
    }

    @PostMapping(value = "updateProfile")
    public String updateProfile(@RequestBody Map<String, String> json) {
        // Change id-temp.jpg file to id.jpg
        return "placeholder";
    }

    @GetMapping(value = "getProfileImage",
        produces = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public MultipartFile getImage(HttpServletRequest req) {

        return null;
    }

    @GetMapping(value = "searchTutor")
    public List<UserProfile> searchTutor(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        String course = (String) json.get("class");
        List<Integer> slots = (List<Integer>) json.get("availability");
        return dao.findTutors(course, slots);
    }

    @PostMapping(value = "sendRequest")
    public String sendRequest(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        
        return "success";
    }

    @GetMapping(value = "getNotifications")
    public Object getNotifications(HttpServletRequest req) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return null;
        }
        Integer id = Integer.valueOf(idStr);
        return dao.getNotifications(id);
    }

    @PostMapping(value = "acceptRequest")
    public String acceptRequest(HttpServletRequest req, @RequestBody Integer requestId) {

        return "success";
    }

    @PostMapping(value = "rejectRequest")
    public String rejectRequest(HttpServletRequest req, @RequestBody Integer requestId) {
        return "success";
    }

    @GetMapping(value = "getTutors")
    public List<UserProfile> getTutors() {

        return null;
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
