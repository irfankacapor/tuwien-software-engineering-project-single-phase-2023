import {Component, Input, OnInit} from '@angular/core';
import {TournamentDetailParticipantDto, TournamentStandingsTreeDto} from "../../../../dto/tournament";
import {of} from "rxjs";

enum TournamentBranchPosition {
  FINAL_WINNER,
  UPPER,
  LOWER,
}

@Component({
  selector: 'app-tournament-standings-branch',
  templateUrl: './tournament-standings-branch.component.html',
  styleUrls: ['./tournament-standings-branch.component.scss']
})
export class TournamentStandingsBranchComponent implements OnInit {
  protected readonly TournamentBranchPosition = TournamentBranchPosition;
  @Input() branchPosition = TournamentBranchPosition.FINAL_WINNER;
  @Input() treeBranch: TournamentStandingsTreeDto | undefined;
  @Input() allParticipants: TournamentDetailParticipantDto[] = [];

  ngOnInit() {

  }

  get isUpperHalf(): boolean {
    return this.branchPosition === TournamentBranchPosition.UPPER;
  }

  get isLowerHalf(): boolean {
    return this.branchPosition === TournamentBranchPosition.LOWER;
  }

  get isFinalWinner(): boolean {
    return this.branchPosition === TournamentBranchPosition.FINAL_WINNER;
  }

  suggestions = (input: string) => {
    // The candidates are either the participants of the previous round matches in this branch
    // or, if this is the first round, all participant horses
    const allCandidates =
      this.treeBranch?.branches?.map(b => b.thisParticipant)
      ?? this.allParticipants;
    const results = allCandidates
        .filter(x => !!x)
        .map(x => <TournamentDetailParticipantDto><unknown>x)
        .filter((x) =>
            x.name.toUpperCase().match(new RegExp(`.*${input.toUpperCase()}.*`)));
    return of(results);
  };

  public formatParticipant(participant: TournamentDetailParticipantDto | null): string {
    if (participant) {
      const dateOfBirth = participant.dateOfBirth instanceof Date
        ? participant.dateOfBirth
        : new Date(participant.dateOfBirth);

      return `${participant.name} (${dateOfBirth.toLocaleDateString()})`;
    } else {
      return "";
    }
  }
}
