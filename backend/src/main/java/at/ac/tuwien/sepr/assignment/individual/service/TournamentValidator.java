package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TournamentValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void validateForTournamentCreate(TournamentCreateDto tournament) throws ValidationException {
    LOG.trace("validateForCreate{}", tournament);
    List<String> validationErrors = new ArrayList<>();
    if (tournament.startDate().isAfter(tournament.endDate())) {
      validationErrors.add("Tournament start date is after the tournament end date.");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of tournament for create failed", validationErrors);
    }
  }

  public void validateForStandingsUpdate(TournamentStandingsDto standingsToSet) throws ValidationException {
    LOG.trace("validateForStandingsUpdate{}", standingsToSet);
    List<String> validationErrors = new ArrayList<>();

    if (standingsToSet == null) {
      validationErrors.add("No standings given.");
    } else {
      if (!isValidFirstRound(standingsToSet)) {
        validationErrors.add("One participant can appear only once in the first round.");
      }
      if (!isValidWinner(standingsToSet.getTree())) {
        validationErrors.add("A winner cannot be set for a match that hasn't finished.");
      }
    }
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of standings for update failed", validationErrors);
    }
  }

  private static boolean isValidFirstRound(TournamentStandingsDto standingsToSet) {
    List<TournamentParticipantDto> firstRoundParticipants = new ArrayList<>();
    validateFirstRoundRecursive(standingsToSet.getTree(), firstRoundParticipants);
    for (int i = 0; i < firstRoundParticipants.size(); i++) {
      for (int j = i + 1; j < firstRoundParticipants.size(); j++) {
        if (firstRoundParticipants.get(i).equals(firstRoundParticipants.get(j))) {
          return false;
        }
      }
    }
    return true;
  }

  private static void validateFirstRoundRecursive(TournamentStandingsTreeDto tree, List<TournamentParticipantDto> firstRoundParticipants) {
    if (tree == null) {
      return;
    }
    if (tree.getBranches() == null) {
      if (tree.getThisParticipant() != null) {
        firstRoundParticipants.add(tree.getThisParticipant());
      }
    } else {
      validateFirstRoundRecursive(tree.getBranches()[0], firstRoundParticipants);
      validateFirstRoundRecursive(tree.getBranches()[1], firstRoundParticipants);
    }
  }

  private static boolean isValidWinner(TournamentStandingsTreeDto tree) {
    if (tree.getBranches() == null) {
      return true;
    }
    if (tree.getThisParticipant() != null) {
      if (tree.getBranches()[0].getThisParticipant() == null || tree.getBranches()[1].getThisParticipant() == null) {
        return false;
      }
      return true;
    }
    return isValidWinner(tree.getBranches()[0]) && isValidWinner(tree.getBranches()[1]);
  }
}
