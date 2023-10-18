package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  public TournamentDetailDto create(TournamentCreateDto tournament) throws NotFoundException, ValidationException {
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

  @Override
  public TournamentStandingsDto getStandings(long id) throws NotFoundException, FatalException {
    LOG.trace("get standings of tournament {}", id);
    var tournament = dao.getById(id);
    TournamentDetailDto tournamentDto = mapper.entityToDetailDto(tournament);
    List<TournamentParticipantDto> sortedParticipants = new ArrayList<>(Collections.nCopies(tournamentDto.participants().size(), null));
    List<Long> ids = new ArrayList<>();
    for (TournamentParticipantDto participant : tournamentDto.participants()) {
      if (participant.getEntryNumber() > -1) {
        sortedParticipants.set(participant.getEntryNumber(), participant);
        ids.add(participant.getHorseId());
      }
    }
    for (int i = 0; i < sortedParticipants.size(); i++) {
      if (sortedParticipants.get(i) == null) {
        for (int j = 0; j < tournamentDto.participants().size(); j++) {
          TournamentParticipantDto currentParticipant = tournamentDto.participants().get(j);
          if (currentParticipant.getEntryNumber() == -1 && !ids.contains(currentParticipant.getHorseId())) {
            sortedParticipants.set(i, currentParticipant);
            ids.add(currentParticipant.getHorseId());
            break;
          }
        }
      }
    }
    return new TournamentStandingsDto(new TournamentDetailDto(
        tournamentDto.id(),
        tournamentDto.name(),
        tournamentDto.startDate(),
        tournamentDto.endDate(),
        sortedParticipants));
  }

  @Override
  public TournamentStandingsDto setStandings(TournamentStandingsDto standingsToSet, long id) throws ValidationException, NotFoundException {
    LOG.trace("set standings of tournament {} as {}", id, standingsToSet);
    validator.validateForStandingsUpdate(standingsToSet);
    List<TournamentParticipantDto> updatedTournamentParticipants = new ArrayList<>(0);
    setParticipantEntryNumbersRecursive(standingsToSet.getTree(), 0, updatedTournamentParticipants);
    List<Long> ids = new ArrayList<>();
    for (TournamentParticipantDto participant : updatedTournamentParticipants) {
      if (participant != null) {
        ids.add(participant.getHorseId());
      }
    }
    for (int i = 0; i < updatedTournamentParticipants.size(); i++) {
      if (updatedTournamentParticipants.get(i) == null) {
        for (int j = 0; j < standingsToSet.getParticipants().size(); j++) {
          TournamentParticipantDto currentParticipant = standingsToSet.getParticipants().get(j);
          if (currentParticipant.getEntryNumber() == -1 && !ids.contains(currentParticipant.getHorseId())) {
            updatedTournamentParticipants.set(i, currentParticipant);
            ids.add(currentParticipant.getHorseId());
            break;
          }
        }
      }
    }
    standingsToSet.setParticipants(updatedTournamentParticipants);
    setRoundReached(standingsToSet);
    var standings = dao.setStandings(standingsToSet, id);
    return mapper.entityToStandingsDto(standings);
  }

  private static int setParticipantEntryNumbersRecursive(TournamentStandingsTreeDto node,
                                                         int entryNumber,
                                                         List<TournamentParticipantDto> updatedTournamentParticipants) {
    if (node == null) {
      return entryNumber;
    }
    if (node.getBranches() == null) {
      if (node.getThisParticipant() != null) {
        node.getThisParticipant().setEntryNumber(entryNumber);
        updatedTournamentParticipants.add(node.getThisParticipant());
      } else {
        updatedTournamentParticipants.add(null);
      }
      entryNumber++;
    } else {
      entryNumber = setParticipantEntryNumbersRecursive(node.getBranches()[0], entryNumber, updatedTournamentParticipants);
      entryNumber = setParticipantEntryNumbersRecursive(node.getBranches()[1], entryNumber, updatedTournamentParticipants);
    }
    return entryNumber;
  }


  private void setRoundReached(TournamentStandingsDto standings) {
    HashMap<Long, Integer> counts = new HashMap<>();
    countRounds(standings.getTree(), counts);
    for (TournamentParticipantDto participant : standings.getParticipants()) {
      participant.setRoundReached(counts.get(participant.getHorseId()) == null ? 0 : counts.get(participant.getHorseId()));
    }
  }

  private void countRounds(TournamentStandingsTreeDto tree, Map<Long, Integer> counts) {
    if (tree == null) {
      return;
    }
    if (tree.getThisParticipant() != null) {
      Long horseId = tree.getThisParticipant().getHorseId();
      counts.put(horseId, (counts.get(horseId) != null ? counts.get(horseId) : 0) + 1);
    }
    if (tree.getBranches() != null) {
      for (TournamentStandingsTreeDto branch : tree.getBranches()) {
        countRounds(branch, counts);
      }
    }
  }


}
