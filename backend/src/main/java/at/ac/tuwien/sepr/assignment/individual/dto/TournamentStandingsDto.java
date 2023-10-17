package at.ac.tuwien.sepr.assignment.individual.dto;

import java.util.List;

public record TournamentStandingsDto(
    long id,
    String name,
    List<TournamentParticipantDto> participants,
    TournamentStandingsTreeDto tree
) {
}
