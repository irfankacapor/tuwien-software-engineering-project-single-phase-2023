package at.ac.tuwien.sepr.assignment.individual.dto;

import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentStandingsTree;

import java.util.List;

public class TournamentStandingsDto {
  long id;
  String name;
  List<TournamentParticipantDto> participants;
  TournamentStandingsTreeDto tree;

  public TournamentStandingsDto() {
  }

  public TournamentStandingsDto(long id, String name, List<TournamentParticipantDto> participants, TournamentStandingsTreeDto tree) {
    this.id = id;
    this.name = name;
    this.participants = participants;
    this.tree = tree;
  }

  public TournamentStandingsDto(TournamentDetailDto tournament) {
    this.id = tournament.id();
    this.name = tournament.name();
    this.participants = tournament.participants();
    this.tree = new TournamentStandingsTreeDto(tournament.participants(), 4);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<TournamentParticipantDto> getParticipants() {
    return participants;
  }

  public void setParticipants(List<TournamentParticipantDto> participants) {
    this.participants = participants;
  }

  public TournamentStandingsTreeDto getTree() {
    return tree;
  }

  public void setTree(TournamentStandingsTreeDto tree) {
    this.tree = tree;
  }
}
