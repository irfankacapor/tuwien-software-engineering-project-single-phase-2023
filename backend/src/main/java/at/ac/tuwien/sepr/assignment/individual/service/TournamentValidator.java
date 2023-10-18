package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

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
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of standings for update failed", validationErrors);
    }
  }
}
