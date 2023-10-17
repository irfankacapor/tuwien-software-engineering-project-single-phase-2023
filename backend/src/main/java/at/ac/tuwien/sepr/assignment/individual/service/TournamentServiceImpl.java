package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@Service
public class TournamentServiceImpl implements TournamentService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final TournamentMapper mapper;
  private final TournamentDao dao;
  private final TournamentValidator validator;

  public TournamentServiceImpl(TournamentDao dao, TournamentMapper mapper, TournamentValidator validator) {
    this.dao = dao;
    this.mapper = mapper;
    this.validator = validator;
  }

  @Override
  public Stream<TournamentListDto> search(TournamentSearchDto searchParameters) {
    LOG.trace("search({})", searchParameters);
    var tournaments = dao.search(searchParameters);
    return tournaments.stream()
        .map(tournament -> mapper.entityToListDto(tournament));
  }

  @Override
  public TournamentDetailDto create(TournamentDetailDto tournament) throws NotFoundException, ValidationException {
    LOG.trace("create({})", tournament);
    validator.validateForTournamentCreate(tournament);
    var addedTournament = dao.create(tournament);
    return mapper.entityToDetailDto(addedTournament);
  }

  @Override
  public TournamentStandingsDto generateFirstRound(long id) throws NotFoundException, FatalException {
    LOG.trace("generate first round of tournament {}", id);
    var standings = dao.generateFirstRound(id);
    return mapper.entityToStandingsDto(standings);
  }
}
