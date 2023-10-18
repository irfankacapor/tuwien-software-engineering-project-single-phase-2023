package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TournamentParticipantDto {

  long horseId;
  String name;
  LocalDate dateOfBirth;
  int entryNumber;
  int roundReached;

  public TournamentParticipantDto() {
  }

  public TournamentParticipantDto(long horseId, String name, LocalDate dateOfBirth, int entryNumber, int roundReached) {
    this.horseId = horseId;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.entryNumber = entryNumber;
    this.roundReached = roundReached;
  }

  public long getHorseId() {
    return horseId;
  }

  public void setHorseId(long horseId) {
    this.horseId = horseId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public int getEntryNumber() {
    return entryNumber;
  }

  public void setEntryNumber(int entryNumber) {
    this.entryNumber = entryNumber;
  }

  public int getRoundReached() {
    return roundReached;
  }

  public void setRoundReached(int roundReached) {
    this.roundReached = roundReached;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TournamentParticipantDto that = (TournamentParticipantDto) o;
    return horseId == that.horseId;
  }
}
