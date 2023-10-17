package at.ac.tuwien.sepr.assignment.individual.entity;

import java.util.ArrayList;
import java.util.List;

public class TournamentStandings {
  long id;
  String name;
  List<TournamentParticipant> participants;
  TournamentStandingsTree branches;

  public TournamentStandings(Tournament tournament) {
    this.id = tournament.getId();
    this.name = tournament.getName();
    this.participants = tournament.getParticipants();
    this.branches = new TournamentStandingsTree(participants, 4);
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<TournamentParticipant> getParticipants() {
    return participants;
  }

  public TournamentStandingsTree getBranches() {
    return branches;
  }
}