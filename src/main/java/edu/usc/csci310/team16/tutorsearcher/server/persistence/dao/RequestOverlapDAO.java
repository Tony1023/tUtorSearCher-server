package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.RequestOverlap;

import java.util.List;

public interface RequestOverlapDAO {
    void addOverlaps(Request request, List<Integer> overlap);
    void removeOverlaps(Request request);
}
