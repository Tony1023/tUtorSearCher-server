package edu.usc.csci310.team16.tutorsearcher.server;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/user")
public class UserOperations {

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
    public String updateProfile(@RequestBody Map<String, Object> json) {
        MySQLConfig.getDAO().updateUserProfile(json);
        return "Success";
    }

    @GetMapping(value = "getProfileImage/{userId}",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getImage(HttpServletRequest req, @PathVariable String userId) {
        try {
            File file = new File(home + profileImageDir + "/" + userId + "_upload");
            InputStream in = new FileInputStream(file);
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            File file = new File(home + profileImageDir + "/default");
            try {
                InputStream in = new FileInputStream(file);
                return IOUtils.toByteArray(in);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping(value = "searchTutor")
    public List<UserProfile> searchTutor(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        String course = (String) json.get("class");
        List<Integer> slots = (List<Integer>) json.get("availability");
        return MySQLConfig.getDAO().findTutors(course, slots);
    }

    @GetMapping(value = "getNotifications")
    public Object getNotifications(HttpServletRequest req) {
        String idStr = req.getHeader("user-id");
        if (idStr == null) {
            return null;
        }
        Integer id = Integer.valueOf(idStr);
        return MySQLConfig.getDAO().getNotifications(id);
    }

    @PostMapping(value = "sendRequest")
    public String sendRequest(HttpServletRequest req, @RequestBody Map<String, Object> json) {
        Integer tutee = (Integer) json.get("tutee_id");
        Integer tutor = (Integer) json.get("tutor_id");
        String course = (String) json.get("course");
        List<Integer> overlap = (List<Integer>) json.get("availability");
        int res = MySQLConfig.getDAO().addRequest(tutee, tutor, course, overlap);
        if (res == 1) {
            return "Success";
        } else if (res == 0) {
            return "Cannot request the same tutor twice";
        } else if (res == -1) {
            return "Cannot request for the same course twice";
        } else {
            return "Failure";
        }
    }

    @PostMapping(value = "acceptRequest")
    public String acceptRequest(HttpServletRequest req, @RequestBody Integer requestId) {
        try {
            lock.lock();
            Map<String, Object> request = MySQLConfig.getDAO().getRequestById(requestId);
            if ((Integer) request.get("req_status") == 3) {
                return "Tutee taken";
            }
            MySQLConfig.getDAO().acceptRequest(requestId);
        } finally {
            lock.unlock();
        }
        return "Success";
    }

    @PostMapping(value = "rejectRequest")
    public String rejectRequest(HttpServletRequest req, @RequestBody Integer requestId) {
        Map<String, Object> request = MySQLConfig.getDAO().getRequestById(requestId);
        if ((Integer) request.get("req_status") == 0) {
            MySQLConfig.getDAO().rejectRequest(requestId);
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
        Integer id = Integer.valueOf(idStr);
        return MySQLConfig.getDAO().getTutors(id);
    }

    @PostMapping(value = "getRating",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Double getRating(@RequestParam(value="tutor_id") Integer tutorId, @RequestParam(value="tutee_id") Integer tuteeId) {
        return MySQLConfig.getDAO().getRating(tutorId, tuteeId); // TODO: verify that returning null works
    }

    @PostMapping(value = "rateTutor",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String rateTutor(@RequestParam(value="tutor_id") Integer tutorId, @RequestParam(value="tutee_id") Integer tuteeId, @RequestParam(value="rating") Double rating) {
        MySQLConfig.getDAO().updateRating(tutorId, tuteeId, rating);
        return "Success";
    }



}
