package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class TournamentMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a tournament entity object to a {@link TournamentListDto}.
   *
   * @param tournament the tournament to convert
   * @return the converted {@link TournamentListDto}
   */
  public TournamentListDto entityToListDto(Tournament tournament) {
    LOG.trace("entityToListDto({})", tournament);
    if (tournament == null) {
      return null;
    }

    return new TournamentListDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getStartDate(),
        tournament.getEndDate()
    );
  }

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

    ArrayList<TournamentParticipantDto> participants = new ArrayList<>();
    for (TournamentParticipant participant : tournament.getParticipants()) {
      participants.add(new TournamentParticipantDto(
          participant.getId(),
          participant.getName(),
          participant.getDateOfBirth(),
          participant.getEntryNumber(),
          participant.getRoundReached()));
    }

    List<TournamentParticipantDto> newTournamentParticipants = new ArrayList<>(participants);


    return new TournamentDetailDto(
      tournament.getId(),
      tournament.getName(),
      tournament.getStartDate(),
      tournament.getEndDate(),
      newTournamentParticipants
    );
  }

}
