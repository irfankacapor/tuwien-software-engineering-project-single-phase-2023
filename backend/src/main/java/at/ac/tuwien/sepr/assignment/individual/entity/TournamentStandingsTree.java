package at.ac.tuwien.sepr.assignment.individual.entity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class TournamentStandingsTree {
  TournamentParticipant thisParticipant;
  TournamentStandingsTree[] branches;

  public TournamentStandingsTree(List<TournamentParticipant> participants, int numOfRounds) {
    this.branches = new TournamentStandingsTree[2];
    int maxRoundReached = 0;
    for (TournamentParticipant participant : participants) {
      maxRoundReached = Math.max(participant.getRoundReached(), maxRoundReached);
    }
    this.createTournamentStandingsRecursive(participants, maxRoundReached, numOfRounds);
  }

  public TournamentStandingsTree createTournamentStandingsRecursive(List<TournamentParticipant> participants, int maxRoundReached, int currentRound) {
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
      this.branches[0] = new TournamentStandingsTree(participants.subList(0, participants.size() / 2), currentRound - 1);
      this.branches[1] = new TournamentStandingsTree(participants.subList(participants.size() / 2, participants.size()), currentRound - 1);
    } else {
      TournamentParticipant bestParticipant = participants.get(0);
      for (int i = 1; i < participants.size(); i++) {
        if (participants.get(i).getRoundReached() > bestParticipant.getRoundReached()) {
          bestParticipant = participants.get(i);
        }
      }
      this.thisParticipant = bestParticipant;
      this.branches[0] = new TournamentStandingsTree(participants.subList(0, participants.size() / 2), currentRound - 1);
      this.branches[1] = new TournamentStandingsTree(participants.subList(participants.size() / 2, participants.size()), currentRound - 1);
    }
    return this;
  }

  public TournamentParticipant getThisParticipant() {
    return thisParticipant;
  }

  public TournamentStandingsTree[] getBranches() {
    return branches;
  }
}
