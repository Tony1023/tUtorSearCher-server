package edu.usc.csci310.team16.tutorsearcher.server;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.NotificationAdapter;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.UserProfile;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.NotificationService;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.RatingService;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.RequestService;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/user")
public class UserOperations {

    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RatingService ratingService;

    public static final String home = System.getProperty("catalina.home");
    public static final String profileImageDir = "/disk/tutorsearcher/images";

    private static Lock lock = new ReentrantLock();

    @PostMapping(value = "updateProfileImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String receiveImage(HttpServletRequest req, MultipartFile image) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return "Failure";
        }
        try {
            File file = new File(home + profileImageDir, idStr + "_upload");
            if (file.createNewFile()) {
                image.transferTo(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failure";
        }
        return "Success";
    }

    @PostMapping(value = "updateProfile")
    public String updateProfile(@RequestBody UserProfile profile) {
        userService.saveUser(profile);
        return "Success";
    }

    @PostMapping(value = "searchTutor")
    public List<UserProfile> searchTutor(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        String course = (String) json.get("class");
        List<Integer> slots = (List<Integer>) json.get("availability");
        return userService.searchTutors(course, slots)
                .stream()
                .map(UserProfile::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "getNotifications")
    public Object getNotifications(HttpServletRequest req) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return null;
        }
        long id = Long.parseLong(idStr);
        return notificationService.getNotifications(id)
                .stream()
                .map(NotificationAdapter::new)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "sendRequest")
    public String sendRequest(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        Integer tutee = (Integer) json.get("tutee_id");
        Integer tutor = (Integer) json.get("tutor_id");
        String course = (String) json.get("course");
        List<Integer> overlap = (List<Integer>) json.get("availability");
        int res = requestService.addRequest(tutee, tutor, course, overlap);
        if (res == 0) {
            return "Success";
        } else if (res == 1) {
            return "Cannot request the same tutor twice";
        } else if (res == -1) {
            return "Cannot request for the same course twice";
        } else {
            return "Failure";
        }
    }

    @PostMapping(value = "acceptRequest")
    public String acceptRequest(HttpServletRequest req, @RequestBody Long requestId) {
        try {
            lock.lock();
            Request request = requestService.findById(requestId);
            if (request.getStatus() == 3) {
                return "Tutee taken";
            } else if (request.getStatus() != 0) {
                return "You already took care of this request";
            }
            requestService.acceptRequest(request);
        } finally {
            lock.unlock();
        }
        return "Success";
    }

    @PostMapping(value = "rejectRequest")
    public String rejectRequest(HttpServletRequest req, @RequestBody Integer requestId) {
        Request request = requestService.findById(requestId);
        if (request.getStatus() == 0) {
            requestService.rejectRequest(request);
            return "Success";
        } else {
            return "Request already resolved";
        }
    }

    @GetMapping(value = "getTutors")
    public List<UserProfile> getTutors(HttpServletRequest req) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return null;
        }
        long id = Long.parseLong(idStr);
        return userService.getTutors(id)
                .stream()
                .map(UserProfile::new)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "getRating",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Double getRating(@RequestParam(value="tutor_id") Integer tutorId, @RequestParam(value="tutee_id") Integer tuteeId) {
        return ratingService.getRating(tuteeId, tutorId); // TODO: verify that returning null works
    }

    @PostMapping(value = "rateTutor",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String rateTutor(@RequestParam(value="tutor_id") Integer tutorId, @RequestParam(value="tutee_id") Integer tuteeId, @RequestParam(value="rating") Double rating) {
        ratingService.updateRating(tuteeId, tutorId, rating);
        return "Success";
    }


    @GetMapping(value = "getNotificationCount")
    public Integer getNotificationCount(HttpServletRequest req) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return 0;
        }
        long id = Long.parseLong(idStr);
        return notificationService.getUnpushedNotificationCount(id);
    }

}
