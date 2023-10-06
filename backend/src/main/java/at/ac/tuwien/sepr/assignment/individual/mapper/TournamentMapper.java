package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSelectionDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

@Component
public class TournamentMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  /**
   * Convert a tournament entity object to a {@link TournamentDetailDto}.
   *
   * @param tournament the tournament to convert
   * @return the converted {@link TournamentDetailDto}
   */
  public TournamentDetailDto entityToDetailDto(Tournament tournament) {
    LOG.trace("entityToDto({})", tournament);
    if (tournament == null) {
      return null;
    }

    ArrayList<HorseSelectionDto> participants = new ArrayList<HorseSelectionDto>();
    for (Horse participant : tournament.getParticipants()) {
      participants.add(new HorseSelectionDto(participant.getId(), participant.getName(), participant.getDateOfBirth()));
    }

    HorseSelectionDto[] newTournamentParticipants = participants.toArray(new HorseSelectionDto[0]);

    return new TournamentDetailDto(
      tournament.getId(),
      tournament.getName(),
      tournament.getStartDate(),
      tournament.getEndDate(),
      newTournamentParticipants
    );
  }
}
