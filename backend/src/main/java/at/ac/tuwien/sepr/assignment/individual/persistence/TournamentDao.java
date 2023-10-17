package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentStandings;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;

import java.util.Collection;

/**
 * Data Access Object for tournaments.
 * Implements access functionality to the application's persistent data store regarding tournaments.
 */
public interface TournamentDao {

  /**
   * Get the tournaments that match the given search parameters.
   * Parameters that are {@code null} are ignored.
   * The date range is considered a match, if it has at least one common day in the date range of the tournament
   *
   * @param searchParameters the parameters to use in searching
   * @return the tournaments where all given parameters match
   */
  Collection<Tournament> search(TournamentSearchDto searchParameters);

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

  /**
   * Get the first round matches of a tournament.
   *
   * @param id the id of the tournament whose first round matches are wanted.
   * @return the new standings of the tournament.
   */
  TournamentStandings generateFirstRound(long id) throws NotFoundException, FatalException;

  /**
   * Get the tournament with the given id
   *
   * @param id of the tournament to get.
   * @return the tournament with the given id.
   */
  Tournament getById(long id) throws NotFoundException;

}
