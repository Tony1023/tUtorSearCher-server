package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.RequestOverlap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RequestOverlapDAOImpl extends AbstractDAO implements RequestOverlapDAO {
    @Override
    public void addOverlaps(Request request, List<Integer> overlap) {
        for (Integer slot: overlap) {
            em.persist(new RequestOverlap(request, slot));
        }
    }

    @Override
    public void removeOverlaps(Request request) {
        for (RequestOverlap overlap: request.getOverlap()) {
            em.remove(overlap);
        }
    }
}
