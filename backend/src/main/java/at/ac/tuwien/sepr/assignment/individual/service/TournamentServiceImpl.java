package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class TournamentServiceImpl implements TournamentService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final TournamentMapper mapper;
  private final TournamentDao dao;

  public TournamentServiceImpl(TournamentDao dao, TournamentMapper mapper) {
    this.dao = dao;
    this.mapper = mapper;
  }

  @Override
  public TournamentDetailDto create(TournamentDetailDto tournament) throws NotFoundException {
    LOG.trace("create({})", tournament);
    var addedTournament = dao.create(tournament);
    return mapper.entityToDetailDto(addedTournament);
  }
}
