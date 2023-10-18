package at.ac.tuwien.sepr.assignment.individual.entity;

import java.time.LocalDate;

public class TournamentParticipant {
  long id;
  String name;
  LocalDate dateOfBirth;
  int entryNumber;
  int roundReached;

  public long getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }
  public int getEntryNumber() {
    return entryNumber;
  }
  public int getRoundReached() {
    return roundReached;
  }
  public TournamentParticipant setId(Long id) {
    this.id = id;
    return this;
  }
  public TournamentParticipant setName(String name) {
    this.name = name;
    return this;
  }
  public TournamentParticipant setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }
  public TournamentParticipant setRoundReached(int roundReached) {
    this.roundReached = roundReached;
    return this;
  }
  public TournamentParticipant setEntryNumber(int entryNumber) {
    this.entryNumber = entryNumber;
    return this;
  }

}
