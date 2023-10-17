package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

public record TournamentParticipantDto(
    long horseId,
    String name,
    LocalDate dateOfBirth,
    long entryNumber,
    int roundReached
) {
}
