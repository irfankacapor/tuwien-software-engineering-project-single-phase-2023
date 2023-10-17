import {Component, OnInit} from '@angular/core';
import {debounceTime, map, Observable, Subject} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {TournamentListDto, TournamentSearchParams, TournamentStandingsDto} from "../../dto/tournament";
import {TournamentService} from "../../service/tournament.service";

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})
export class TournamentComponent implements OnInit {
  search = false;
  tournaments: TournamentListDto[] = [];
  bannerError: string | null = null;
  searchParams: TournamentSearchParams = {};
  searchStartDate: string | null = null;
  searchEndDate: string | null = null;
  searchChangedObservable = new Subject<void>();

  constructor(
    private service: TournamentService,
    private notification: ToastrService,
  ) { }

  ngOnInit(): void {
    this.reloadTournaments();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadTournaments()});
  }

  reloadTournaments() {
    if (this.searchStartDate) {
      this.searchParams.startDate = new Date(this.searchStartDate);
    }
    if (this.searchEndDate) {
      this.searchParams.endDate = new Date(this.searchEndDate);
    }
    this.service.search(this.searchParams)
      .subscribe({
        next: data  => {
          this.tournaments = data;
        },
        error: error => {
          console.error('Error fetching tournaments', error);
          this.bannerError = 'Could not fetch tournaments: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Tournaments');
        }
      });
  }
  searchChanged(): void {
    this.searchChangedObservable.next();
  }
}
