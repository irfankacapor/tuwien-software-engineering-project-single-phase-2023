package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;

import java.util.stream.Stream;

/**
 * Service for working with horses
 */
public interface TournamentService {

  /**
   * Create a new tournament with
   *
   * @param tournament the tournament to be added
   * @return the added tournament
   * @throws NotFoundException when a horse that does not exist in the system is added to the tournament
   */
  TournamentDetailDto create(TournamentDetailDto tournament) throws NotFoundException;
}
