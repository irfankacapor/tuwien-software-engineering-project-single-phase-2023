package at.ac.tuwien.sepr.assignment.individual.dto;

public record TournamentStandingsTreeDto(
    TournamentParticipantDto thisParticipant,
    TournamentStandingsTreeDto[] branches
) {
}
