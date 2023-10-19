package at.ac.tuwien.sepr.assignment.individual.dto;

import java.util.List;

public class TournamentStandingsTreeDto {
  TournamentParticipantDto thisParticipant;
  TournamentStandingsTreeDto[] branches;

  public TournamentStandingsTreeDto() {
  }

  public TournamentStandingsTreeDto(TournamentParticipantDto thisParticipant, TournamentStandingsTreeDto[] branches) {
    this.thisParticipant = thisParticipant;
    this.branches = branches;
  }
  public TournamentStandingsTreeDto(List<TournamentParticipantDto> participants, int numOfRounds) {
    this.branches = new TournamentStandingsTreeDto[2];
    int maxRoundReached = 0;
    for (TournamentParticipantDto participant : participants) {
      maxRoundReached = Math.max(participant.getRoundReached(), maxRoundReached);
    }
    this.createTournamentStandingsRecursive(participants, maxRoundReached, numOfRounds);
  }

  public TournamentStandingsTreeDto createTournamentStandingsRecursive(List<TournamentParticipantDto> participants, int maxRoundReached, int currentRound) {
    if (currentRound < 2) {
      if (maxRoundReached == 0) {
        thisParticipant = null;
      } else {
        this.thisParticipant = participants.get(0);
      }
      this.branches = null;
      return this;
    }
    if (maxRoundReached < currentRound) {
      this.thisParticipant = null;
      this.branches[0] = new TournamentStandingsTreeDto(participants.subList(0, participants.size() / 2), currentRound - 1);
      this.branches[1] = new TournamentStandingsTreeDto(participants.subList(participants.size() / 2, participants.size()), currentRound - 1);
    } else {
      TournamentParticipantDto bestParticipant = participants.get(0);
      for (int i = 1; i < participants.size(); i++) {
        if (participants.get(i).getRoundReached() > bestParticipant.getRoundReached()) {
          bestParticipant = participants.get(i);
        }
      }
      this.thisParticipant = bestParticipant;
      this.branches[0] = new TournamentStandingsTreeDto(participants.subList(0, participants.size() / 2), currentRound - 1);
      this.branches[1] = new TournamentStandingsTreeDto(participants.subList(participants.size() / 2, participants.size()), currentRound - 1);
    }
    return this;
  }

  public TournamentParticipantDto getThisParticipant() {
    return thisParticipant;
  }

  public void setThisParticipant(TournamentParticipantDto thisParticipant) {
    this.thisParticipant = thisParticipant;
  }

  public TournamentStandingsTreeDto[] getBranches() {
    return branches;
  }

  public void setBranches(TournamentStandingsTreeDto[] branches) {
    this.branches = branches;
  }
}
