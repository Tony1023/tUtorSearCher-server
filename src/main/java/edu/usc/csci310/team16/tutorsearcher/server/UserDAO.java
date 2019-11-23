package edu.usc.csci310.team16.tutorsearcher.server;


import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.UserProfile;

import java.sql.*;
import java.util.*;
import java.util.Date;

class UserDAO {

    private PreparedStatement credentialsQuery = null;
    private PreparedStatement idQuery = null;
    private PreparedStatement emailQuery = null;
    private PreparedStatement availabilityQuery = null;
    private PreparedStatement courseQuery = null;
    private PreparedStatement tutorClassQuery = null;
    private PreparedStatement avgRatingQuery = null;
    private PreparedStatement verifyTokenQuery = null;
    private PreparedStatement registerCommand = null;
    private PreparedStatement addTokenCommand = null;
    private PreparedStatement ratingQuery = null;
    private PreparedStatement notificationsQuery = null;
    private PreparedStatement addRequestCommand = null;
    private PreparedStatement getRequestQuery = null;
    private PreparedStatement getRequestByIdQuery = null;
    private PreparedStatement getTutorIdsQuery = null;
    private PreparedStatement addNotificationCommand = null;
    private PreparedStatement addRequestOverlapCommand = null;
    private PreparedStatement requestOverlapQuery = null;
    private PreparedStatement acceptedRequestsForCourseQuery = null;
    private PreparedStatement decideRequestCommand = null;
    private PreparedStatement invalidateRequestCommand = null;
    private PreparedStatement updateRatingCommand = null;
    private PreparedStatement updateUserProfileCommand = null;
    private PreparedStatement deleteCourseCommand = null;
    private PreparedStatement addCourseCommand = null;
    private PreparedStatement deleteAvailabilityCommand = null;
    private PreparedStatement addAvailabilityCommand = null;
    private PreparedStatement deleteTutorClassCommand = null;
    private PreparedStatement addTutorClassCommand = null;

    private final long validPeriod = 1 * 24 * 60 * 60 * 1000;

    UserDAO() {
        try {
            Connection connection = MySQLConfig.getConnection();
            credentialsQuery = connection.prepareStatement("SELECT * FROM Users WHERE email=? AND pass_hash=? ");
            idQuery = connection.prepareStatement("SELECT * FROM Users WHERE id=?");
            emailQuery = connection.prepareStatement("SELECT * FROM Users WHERE email=?");
            availabilityQuery = connection.prepareStatement("SELECT slot_num FROM Availability WHERE user_id=? ");
            courseQuery = connection.prepareStatement("SELECT c.course_number FROM Users u, Courses c, UserCourses uc " +
                    "WHERE u.id=? AND u.id=uc.user_id AND c.id=uc.course_id");
            tutorClassQuery = connection.prepareStatement("SELECT c.course_number FROM Users u, Courses c, CourseOffered uc " +
                    "WHERE u.id=? AND u.id=uc.user_id AND c.id=uc.course_id");
            avgRatingQuery = connection.prepareStatement("SELECT avg(ifnull(r.rating, -1)) FROM Users u, Ratings r " +
                    "WHERE u.id=? AND r.tutor_id=u.id");
            verifyTokenQuery = connection.prepareStatement("SELECT u.id FROM Users u, AuthTokens t " +
                    "WHERE u.id=? AND t.auth_token=? AND u.id=t.user_id AND t.date_active > CURDATE()");
            registerCommand = connection.prepareStatement("INSERT INTO Users(email, pass_hash) VALUE(?,?)");
            addTokenCommand = connection.prepareStatement("INSERT INTO AuthTokens(user_id, auth_token, date_added, date_active) VALUE(?,?,?,?)");
            ratingQuery = connection.prepareStatement("SELECT rating FROM Ratings WHERE tutor_id=? AND tutee_id=?");
            notificationsQuery = connection.prepareStatement("SELECT n.id, n.req_id, r.req_status, n.receiver_type, n.sender_id, n.receiver_id FROM Requests r, Notifications n " +
                    "WHERE r.id=n.req_id AND n.receiver_id=?" /*+ " AND n.pushed=0"*/);
            addRequestCommand = connection.prepareStatement("INSERT INTO Requests(tutor_id, tutee_id, course_id) " +
                    "SELECT ?, ?, c.id FROM Courses c WHERE c.course_number=?");
            getRequestQuery = connection.prepareStatement("SELECT * FROM Requests r, Courses c WHERE r.course_id=c.id AND tutor_id=? AND tutee_id=? AND c.course_number=?");
            getRequestByIdQuery = connection.prepareStatement("SELECT * FROM Requests WHERE id=?");
            getTutorIdsQuery = connection.prepareStatement("SELECT tutor_id FROM Requests WHERE tutee_id=? AND req_status=1");
            addNotificationCommand = connection.prepareStatement("INSERT INTO Notifications(req_id, sender_id, receiver_id, receiver_type) VALUES(?,?,?,?)");
            addRequestOverlapCommand = connection.prepareStatement("INSERT INTO RequestOverlap(req_id, slot_num) VALUES(?,?)");
            requestOverlapQuery = connection.prepareStatement("SELECT slot_num FROM RequestOverlap WHERE req_id=?");
            acceptedRequestsForCourseQuery = connection.prepareStatement("SELECT r.id FROM Requests r, Courses c " +
                    "WHERE r.course_id=c.id AND tutee_id=? AND c.course_number=? AND req_status=1");
            decideRequestCommand = connection.prepareStatement("UPDATE Requests SET req_status=? WHERE id=?");
            invalidateRequestCommand = connection.prepareStatement("UPDATE Requests SET req_status=3 WHERE tutee_id=? AND course_id=? AND NOT id=?");
            updateRatingCommand = connection.prepareStatement("REPLACE INTO Ratings(tutor_id, tutee_id, rating) VALUES (?,?,?)");
            updateUserProfileCommand = connection.prepareStatement("UPDATE Users SET name=?, grade=?, bio=? WHERE id=?");
            deleteCourseCommand = connection.prepareStatement("DELETE FROM UserCourses WHERE user_id=?");
            addCourseCommand = connection.prepareStatement("INSERT INTO UserCourses(user_id, course_id) " +
                    "SELECT ?, c.id FROM Courses c WHERE c.course_number=?");
            deleteTutorClassCommand = connection.prepareStatement("DELETE FROM CourseOffered WHERE user_id=?");
            addTutorClassCommand = connection.prepareStatement("INSERT INTO CourseOffered(user_id, course_id) SELECT ?, c.id FROM Courses c WHERE c.course_number=?");
            deleteAvailabilityCommand = connection.prepareStatement("DELETE FROM Availability WHERE user_id=?");
            addAvailabilityCommand = connection.prepareStatement("INSERT INTO Availability(user_id, slot_num) VALUES(?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateRating(Integer tutor, Integer tutee, Double rating) {
        try {
            synchronized (updateRatingCommand) {
                updateRatingCommand.setInt(1, tutor);
                updateRatingCommand.setInt(2, tutee);
                updateRatingCommand.setDouble(3, rating);
                updateRatingCommand.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void acceptRequest(Integer id) {
        try {
            ResultSet result;
            synchronized (decideRequestCommand) {
                decideRequestCommand.setInt(1, 1);
                decideRequestCommand.setInt(2, id);
                decideRequestCommand.execute();
            }
            synchronized (getRequestByIdQuery) {
                getRequestByIdQuery.setInt(1, id);
                result = getRequestByIdQuery.executeQuery();
            }
            if (result.next()) {
                synchronized (addNotificationCommand) {
                    addNotificationCommand.setInt(1, result.getInt("id"));
                    addNotificationCommand.setInt(2, result.getInt("tutor_id"));
                    addNotificationCommand.setInt(3, result.getInt("tutee_id"));
                    addNotificationCommand.setInt(4, 1);
                    addNotificationCommand.execute();
                    addNotificationCommand.setInt(2, result.getInt("tutee_id"));
                    addNotificationCommand.setInt(3, result.getInt("tutor_id"));
                    addNotificationCommand.setInt(4, 0);
                    addNotificationCommand.execute();
                }
                synchronized (invalidateRequestCommand) {
                    invalidateRequestCommand.setInt(1, result.getInt("tuee_id"));
                    invalidateRequestCommand.setInt(2, result.getInt("course_id"));
                    invalidateRequestCommand.setInt(3, result.getInt("id"));
                    invalidateRequestCommand.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void rejectRequest(Integer id) {
        try {
            ResultSet result;
            synchronized (getRequestByIdQuery) {
                getRequestByIdQuery.setInt(1, id);
                result = getRequestByIdQuery.executeQuery();
            }
            if (result.next()) {
                int status = result.getInt("req_status");
                if (status == 0) {
                    synchronized (decideRequestCommand) {
                        decideRequestCommand.setInt(1, 2);
                        decideRequestCommand.setInt(2, id);
                        decideRequestCommand.execute();
                    }
                    synchronized (addNotificationCommand) {
                        addNotificationCommand.setInt(1, id);
                        addNotificationCommand.setInt(2, result.getInt("tutor_id"));
                        addNotificationCommand.setInt(3, result.getInt("tutee_id"));
                        addNotificationCommand.setInt(4, 1);
                        addNotificationCommand.execute();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Integer> getAcceptedRequestIds(Integer id, String course) {
        List<Integer> requests = new LinkedList<>();
        try {
            ResultSet result;
            synchronized (acceptedRequestsForCourseQuery) {
                acceptedRequestsForCourseQuery.setInt(1, id);
                acceptedRequestsForCourseQuery.setString(2, course);
                result = acceptedRequestsForCourseQuery.executeQuery();
            }
            while (result.next()) {
                requests.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    Map<String, Object> getRequestById(Integer id) {
        Map<String, Object> request = null;
        try {
            ResultSet result;
            synchronized (getRequestByIdQuery) {
                getRequestByIdQuery.setInt(1, id);
                result = getRequestByIdQuery.executeQuery();
            }
            result.next();
            request = new HashMap<>();
            request.put("tutee_id", result.getInt("tutee_id"));
            request.put("tutor_id", result.getInt("tutor_id"));
            request.put("req_status", result.getInt("req_status"));
            request.put("course_id", result.getInt("course_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return request;
    }

    int addRequest(Integer tutee, Integer tutor, String course, List<Integer> overlap) {
        try {
            ResultSet result;
            int reqId;
            synchronized (getRequestQuery) {
                getRequestQuery.setInt(1, tutor);
                getRequestQuery.setInt(2, tutee);
                getRequestQuery.setString(3, course);
                result = getRequestQuery.executeQuery();
                if (result.next()) {
                    return 0; // already made request
                }
                if (!getAcceptedRequestIds(tutee, course).isEmpty()) {
                    return -1;
                }
                synchronized (addRequestCommand) {
                    addRequestCommand.setInt(1, tutor);
                    addRequestCommand.setInt(2, tutee);
                    addRequestCommand.setString(3, course);
                    addRequestCommand.execute();
                }
                result = getRequestQuery.executeQuery();
                if (result.next()) {
                    reqId = result.getInt("id");
                } else {
                    return -1;
                }
            }
            synchronized (addRequestOverlapCommand) {
                addRequestOverlapCommand.setInt(1, reqId);
                for (Integer slot: overlap) {
                    addRequestOverlapCommand.setInt(2, slot);
                    addRequestOverlapCommand.execute();
                }
            }
            synchronized (addNotificationCommand) {
                addNotificationCommand.setInt(1, reqId);
                addNotificationCommand.setInt(2, tutee);
                addNotificationCommand.setInt(3, tutor);
                addNotificationCommand.setInt(4, 0);
                addNotificationCommand.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }
    
    List<UserProfile> getTutors(Integer id) {
        List<UserProfile> tutors = new LinkedList<>();
        try {
            ResultSet result;
            synchronized (getTutorIdsQuery) {
                getTutorIdsQuery.setInt(1, id);
                result = getTutorIdsQuery.executeQuery();
            }
            while (result.next()) {
                tutors.add(findUserById(result.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutors;
    }

    List<Map<String, Object>> getNotifications(Integer id) {
        List<Map<String, Object>> notifications = new LinkedList<>();
        try {
            ResultSet result;
            synchronized (notificationsQuery) {
                notificationsQuery.setInt(1, id);
                result = notificationsQuery.executeQuery();
                // TODO: optional, set pushed to true and the filter on n.pushed above
            }
            while (result.next()) {
                Map<String, Object> notification = new HashMap<>();
                notification.put("id", result.getInt(1));
                notification.put("receiver_id", result.getInt(6));
                int reqId = result.getInt(2);
                int status = result.getInt(3);
                int type = result.getInt(4);
                int senderId = result.getInt(5);
                notification.put("request_id", reqId);
                notification.put("type", type);

                synchronized (idQuery) {
                    idQuery.setInt(1, senderId);
                    result = idQuery.executeQuery();
                }
                result.next();
                notification.put("sender_id", senderId);
                notification.put("sender_name", result.getString("name"));
                notification.put("request_status", status);
                if (status == 1) {
                    notification.put("msg", result.getString("email"));
                } else {
                    notification.put("msg", "");
                }

                synchronized (requestOverlapQuery) {
                    requestOverlapQuery.setInt(1, reqId);
                    result = requestOverlapQuery.executeQuery();
                }
                List<String> overlap = new ArrayList<>();
                for (int i = 0; i < 28 * 7; ++i) {
                    overlap.add("0");
                }
                while(result.next()) {
                    overlap.set(result.getInt(1), "1");
                }
                String overlapStr = String.join(",", overlap);
                notification.put("overlap", overlapStr);
                notifications.add(notification);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    List<UserProfile> findTutors(String course, List<Integer> slots) {
        List<UserProfile> tutors = new LinkedList<>();
        try {
            ResultSet result;
            StringBuilder query = new StringBuilder();
            query.append("SELECT a.user_id, COUNT(a.slot_num) as overlap FROM Availability a, CourseOffered co, Courses c ");
            query.append("WHERE a.user_id=co.user_id AND co.course_id=c.id AND a.slot_num IN ");
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (Integer slot: slots) {
                sb.append(slot.toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            query.append(sb);
            query.append(" AND c.course_number=\'").append(course).append("\'");
            query.append(" GROUP BY a.user_id ORDER BY overlap DESC");
            PreparedStatement st = MySQLConfig.getConnection().prepareStatement(query.toString());
            result = st.executeQuery();
            while (result.next()) {
                tutors.add(findUserById(result.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (UserProfile tutor: tutors) {
            List<Integer> tutorSlots = tutor.getAvailability();
            List<Integer> overlap = new LinkedList<>();
            for (Integer slot: tutorSlots) {
                if (slots.indexOf(slot) >= 0) {
                    overlap.add(slot);
                }
            }
            tutor.setAvailability(overlap);
        }
        return tutors;
    }

    Double getRating(int tutorId, int tuteeId) {
        Double rating = null;
        try {
            ResultSet result;
            synchronized (ratingQuery) {
                ratingQuery.setInt(1, tutorId);
                ratingQuery.setInt(2, tuteeId);
                result = ratingQuery.executeQuery();
            }
            if (result.next()) {
                rating = result.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rating;
    }

    UserProfile findUserByCredentials(String email, String password) {
        UserProfile user = null;
        try {
            ResultSet res;
            synchronized (credentialsQuery) {
                credentialsQuery.setString(1, email);
                credentialsQuery.setString(2, password);
                res = credentialsQuery.executeQuery();
            }
            user = populateFields(res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    UserProfile findUserById(int id) {
        UserProfile user = null;
        try {
            ResultSet res;
            synchronized (idQuery) {
                idQuery.setInt(1, id);
                res = idQuery.executeQuery();
            }
            user = populateFields(res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    boolean validateUserToken(Integer idIn, String token) {
        boolean verified = false;
        try {
            synchronized (verifyTokenQuery) {
                verifyTokenQuery.setInt(1, idIn);
                verifyTokenQuery.setString(2, token);
                ResultSet result = verifyTokenQuery.executeQuery();
                if (result.next()) {
                    verified = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return verified;
    }

    String getToken(long id) {
        String token = UUID.randomUUID().toString();
        try {
            synchronized (addTokenCommand) {
                Date now = new Date();
                addTokenCommand.setLong(1, id);
                addTokenCommand.setString(2, token);
                addTokenCommand.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                addTokenCommand.setTimestamp(4, new Timestamp(System.currentTimeMillis() + validPeriod));
                addTokenCommand.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     *
     * @param email
     * @param password
     * @return null if already exist
     */
    Integer registerUser(String email, String password) {
        Integer id = null;
        try {
            synchronized (registerCommand) {
                registerCommand.setString(1, email);
                registerCommand.setString(2, password);
                registerCommand.execute();
            }
            synchronized (emailQuery) {
                emailQuery.setString(1, email);
                ResultSet result = emailQuery.executeQuery();
                if (result.next()) {
                    id = result.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    Double getAvgRating(long id) {
        synchronized (avgRatingQuery) {
            try {
                avgRatingQuery.setLong(1, id);
                ResultSet result = avgRatingQuery.executeQuery();
                if (result.next()) {
                    double rating = result.getDouble(1);
                    if (result.wasNull()) {
                        return null;
                    } else {
                        return rating;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private UserProfile populateFields(ResultSet rs) {
        UserProfile user = null;
        try {
            if (rs.next()) {
                user = new UserProfile();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setGrade(rs.getString("grade"));
                user.setBio(rs.getString("bio"));
                synchronized (availabilityQuery) {
                    availabilityQuery.setLong(1, user.getId());
                    ResultSet result = availabilityQuery.executeQuery();
                    List<Integer> slots = new LinkedList<>();
                    while (result.next()) {
                        slots.add(result.getInt(1));
                    }
                    user.setAvailability(slots);
                }
                synchronized (courseQuery) {
                    courseQuery.setLong(1, user.getId());
                    ResultSet result = courseQuery.executeQuery();
                    List<String> courses = new LinkedList<>();
                    while (result.next()) {
                        courses.add(result.getString(1));
                    }
                    user.setCoursesTaken(courses);
                }
                synchronized (tutorClassQuery) {
                    tutorClassQuery.setLong(1, user.getId());
                    ResultSet result = tutorClassQuery.executeQuery();
                    List<String> courses = new LinkedList<>();
                    while (result.next()) {
                        courses.add(result.getString(1));
                    }
                    user.setTutorClasses(courses);
                }
                user.setRating(getAvgRating(user.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return user;
    }

    void updateUserProfile(Map<String, Object> user) {
        Integer id = (Integer) user.get("id");
        try {
            synchronized (updateUserProfileCommand) {
                updateUserProfileCommand.setString(1, (String) user.get("name"));
                updateUserProfileCommand.setString(2, (String) user.get("grade"));
                updateUserProfileCommand.setString(3, (String) user.get("bio"));
                updateUserProfileCommand.setInt(4, id);
                updateUserProfileCommand.execute();
            }
            synchronized (deleteCourseCommand) {
                deleteCourseCommand.setInt(1, id);
                deleteCourseCommand.execute();
                List<String> courses = (List<String>) user.get("coursesTaken");
                for (String course: courses) {
                    synchronized (addCourseCommand) {
                        addCourseCommand.setInt(1, id);
                        addCourseCommand.setString(2, course);
                        addCourseCommand.execute();
                    }
                }
            }
            synchronized (deleteTutorClassCommand) {
                deleteTutorClassCommand.setInt(1, id);
                deleteTutorClassCommand.execute();
                List<String> courses = (List<String>) user.get("tutorClasses");
                for (String course: courses) {
                    synchronized (addTutorClassCommand) {
                        addTutorClassCommand.setInt(1, id);
                        addTutorClassCommand.setString(2, course);
                        addTutorClassCommand.execute();
                    }
                }
            }
            synchronized (deleteAvailabilityCommand) {
                deleteAvailabilityCommand.setInt(1, id);
                deleteAvailabilityCommand.execute();
                List<Integer> slots = (List<Integer>) user.get("availability");
                for (Integer slot: slots) {
                    synchronized (addAvailabilityCommand) {
                        addAvailabilityCommand.setInt(1, id);
                        addAvailabilityCommand.setInt(2, slot);
                        addAvailabilityCommand.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

}
