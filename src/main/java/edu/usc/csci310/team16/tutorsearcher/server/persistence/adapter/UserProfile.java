package edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Availability;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.RequestOverlap;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfile {

    private Long id;
    private String name;
    private String grade;
    private String email;
    private String bio;
    private Double rating;
    private List<Integer> availability;
    private List<String> coursesTaken; //tutor
    private List<String> tutorClasses; //tutor (which can they teach)
    private Set<Integer> disabledSlots;

    public UserProfile() { }

    public UserProfile(User user) {
        id = user.getId();
        name = user.getName();
        grade = user.getGrade();
        email = user.getEmail();
        bio = user.getBio();
        rating = user.getRating();
        availability = user.getAvailability().stream()
                .map(Availability::getSlot)
                .collect(Collectors.toList());
        coursesTaken = user.getCoursesTaken().stream()
                .map(ct -> ct.getCourse().getCourseNumber())
                .collect(Collectors.toList());
        tutorClasses = user.getTutorClasses().stream()
                .map(tc -> tc.getCourse().getCourseNumber())
                .collect(Collectors.toList());
        disabledSlots = new HashSet<>();
        for (Request request: user.getAcceptedRequestsAsTutor()) {
            disabledSlots.addAll(request.getOverlap().stream()
                    .map(RequestOverlap::getSlot)
                    .collect(Collectors.toSet())
            );
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Integer> availability) {
        this.availability = availability;
    }

    public List<String> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(List<String> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public List<String> getTutorClasses() {
        return tutorClasses;
    }

    public void setTutorClasses(List<String> tutorClasses) {
        this.tutorClasses = tutorClasses;
    }

    public Set<Integer> getDisabledSlots() {
        return disabledSlots;
    }

    public UserProfile intersectAvailability(List<Integer> slots) {
        List<Integer> overlap = new ArrayList<>();
        for (Integer slot: slots) {
            if (availability.contains(slot)) {
                overlap.add(slot);
            }
        }
        availability = overlap;
        return this;
    }
}
