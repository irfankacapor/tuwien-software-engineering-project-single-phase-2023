package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;

/**
 * Data Access Object for tournaments.
 * Implements access functionality to the application's persistent data store regarding tournaments.
 */
public interface TournamentDao {

  /**
   * Create a new tournament
   * with the data given in {@code tournament}
   * in the persistent data store
   *
   * @param tournament the tournament to be added
   * @return the created tournament
   * @throws NotFoundException when a horse that is not in the system is added to the tournament
   */
  Tournament create(TournamentDetailDto tournament) throws NotFoundException;
}
