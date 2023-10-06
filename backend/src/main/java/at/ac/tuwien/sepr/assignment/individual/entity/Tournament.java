package at.ac.tuwien.sepr.assignment.individual.entity;

import java.time.LocalDate;

/**
 * Represents a tournament in the persistant data store.
 */
public class Tournament {
  private Long id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private Horse[] participants;

  public Long getId() {
    return id;
  }

  public Tournament setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Tournament setName(String name) {
    this.name = name;
    return this;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public Tournament setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public Tournament setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public Horse[] getParticipants() {
    return participants;
  }

  public Tournament setParticipants(Horse[] participants) {
    this.participants = participants;
    return this;
  }
}
