import {Component, OnInit} from '@angular/core';
import {TournamentStandingsDto} from "../../../dto/tournament";
import {TournamentStandingsTreeDto} from "../../../dto/tournament";
import {TournamentService} from "../../../service/tournament.service";
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";
import {ToastrService} from "ngx-toastr";
import {ErrorFormatterService} from "../../../service/error-formatter.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-tournament-standings',
  templateUrl: './tournament-standings.component.html',
  styleUrls: ['./tournament-standings.component.scss']
})
export class TournamentStandingsComponent implements OnInit {
  standings: TournamentStandingsDto | undefined;

  public constructor(
    private service: TournamentService,
    private errorFormatter: ErrorFormatterService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private location: Location,
  ) {
  }

  public ngOnInit() {

  }

  public submit(form: NgForm) {

  }

  public generateFirstRound() {
    if (this.isStandingSaved()){
      this.notification.error('The first round of the tournament has already been set!');
      return;
    }
    let id: number = Number(this.route.snapshot.paramMap.get('id'))
    let observable: Observable<TournamentStandingsDto>;
    observable = this.service.generateFirstRound(id);
    observable.subscribe({
      next: data => {
        this.standings = data;
        this.notification.success(`Standings of tournament ${data.name} successfully loaded.`);
      },
      error: error => {
        console.error('Error displaying standings!');
      }
    })
  }

  private isStandingSaved = () => {
    if (this.standings == null){
      return false;
    }
    for (let participant of this.standings.participants) {
      if (participant.roundReached) {
        if (participant.roundReached > 0) return true;
      }
    }
    return false;
  }
}
