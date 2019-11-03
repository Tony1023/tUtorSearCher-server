package edu.usc.csci310.team16.tutorsearcher.server;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class UserDAO {

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
    private final long validPeriod = 1 * 24 * 60 * 60 * 1000;

    public UserDAO() {
        try {
            Connection connection = MySQLConfig.getConnection();
            credentialsQuery = connection.prepareStatement("SELECT * FROM Users WHERE email=? AND pass_hash=? ");
            idQuery = connection.prepareStatement("SELECT * FROM Users WHERE id=?");
            emailQuery = connection.prepareStatement("SELECT * FROM Users WHERE email=?");
            availabilityQuery = connection.prepareStatement("SELECT slot_num FROM Availability WHERE user_id=? ");
            courseQuery = connection.prepareStatement("SELECT c.dep, c.num FROM Users u, Courses c, UserCourses uc " +
                    "WHERE u.id=? AND u.id=uc.user_id AND c.id=uc.course_id");
            tutorClassQuery = connection.prepareStatement("SELECT c.dep, c.num FROM Users u, Courses c, CourseOffered uc " +
                    "WHERE u.id=? AND u.id=uc.user_id AND c.id=uc.course_id");
            avgRatingQuery = connection.prepareStatement("SELECT avg(ifnull(r.rating, -1)) FROM Users u, Ratings r " +
                    "WHERE u.id=? AND r.tutor_id=u.id");
            verifyTokenQuery = connection.prepareStatement("SELECT u.id FROM Users u, AuthTokens t " +
                    "WHERE u.id=? AND t.auth_token=? AND u.id=t.user_id AND t.date_active > CURDATE()");
            registerCommand = connection.prepareStatement("INSERT INTO Users(email, pass_hash) VALUE(?,?)");
            addTokenCommand = connection.prepareStatement("INSERT INTO AuthTokens(user_id, auth_token, date_added, date_active) VALUE(?,?,?,?)");
            ratingQuery = connection.prepareStatement("SELECT rating FROM Ratings WHERE tutor_id=? AND tutee_id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Double getRating(int tutorId, int tuteeId) {
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

    public UserProfile findUserByCredentials(String email, String password) {
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

    public UserProfile findUserById(int id) {
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

    public boolean validateUserToken(Integer idIn, String token) {
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

    public String getToken(int id) {
        String token = UUID.randomUUID().toString();
        try {
            synchronized (addTokenCommand) {
                Date now = new Date();
                addTokenCommand.setInt(1, id);
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
    public Integer registerUser(String email, String password) {
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

    public Double getAvgRating(int id) {
        synchronized (avgRatingQuery) {
            try {
                avgRatingQuery.setInt(1, id);
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
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setGrade(rs.getString("grade"));
                user.setPicture_url(rs.getString("picture_url"));
                user.setBio(rs.getString("bio"));
                synchronized (availabilityQuery) {
                    availabilityQuery.setInt(1, user.getId());
                    ResultSet result = availabilityQuery.executeQuery();
                    List<Integer> slots = new LinkedList<>();
                    while (result.next()) {
                        slots.add(result.getInt(1));
                    }
                    user.setAvailability(slots);
                }
                synchronized (courseQuery) {
                    courseQuery.setInt(1, user.getId());
                    ResultSet result = courseQuery.executeQuery();
                    List<String> courses = new LinkedList<>();
                    while (result.next()) {
                        courses.add(result.getString(1) + result.getInt(2));
                    }
                    user.setCoursesTaken(courses);
                }
                synchronized (tutorClassQuery) {
                    tutorClassQuery.setInt(1, user.getId());
                    ResultSet result = tutorClassQuery.executeQuery();
                    List<String> courses = new LinkedList<>();
                    while (result.next()) {
                        courses.add(result.getString(1) + result.getInt(2));
                    }
                    user.setCoursesTaken(courses);
                }
                user.setRating(getAvgRating(user.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return user;
    }



}
