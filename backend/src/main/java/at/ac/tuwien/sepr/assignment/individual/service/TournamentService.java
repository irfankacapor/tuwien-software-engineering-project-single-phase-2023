package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

import java.util.stream.Stream;

/**
 * Service for working with horses
 */
public interface TournamentService {

  /**
   * Search for tournaments in the persistent data store matching all provided fields.
   * The date range is considered a match, if it has at least one common day in the date range of the tournament
   *
   * @param searchParameters the search parameters to use in filtering.
   * @return the tournaments where the given fields match.
   */
  Stream<TournamentListDto> search(TournamentSearchDto searchParameters);


  /**
   * Create a new tournament with
   *
   * @param tournament the tournament to be added
   * @return the added tournament
   * @throws NotFoundException when a horse that does not exist in the system is added to the tournament
   */
  TournamentDetailDto create(TournamentDetailDto tournament) throws NotFoundException, ValidationException;
}
