package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;

import java.util.List;

public interface RequestService {
    int addRequest(long tuteeId, long tutorId, String course, List<Integer> overlap);
    Request findById(long id);
    void acceptRequest(Request request);
    void rejectRequest(Request request);
}
