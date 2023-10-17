package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
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

  public void validateForTournamentCreate(TournamentDetailDto tournament) throws ValidationException {
    LOG.trace("validateForCreate{}", tournament);
    List<String> validationErrors = new ArrayList<>();
    if (tournament.startDate().isAfter(tournament.endDate())) {
      validationErrors.add("Tournament start date is after the tournament end date.");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of tournament for create failed", validationErrors);
    }
  }
}
