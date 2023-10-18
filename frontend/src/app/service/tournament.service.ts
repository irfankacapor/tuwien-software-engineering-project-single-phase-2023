import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map, Observable, tap, throwError} from 'rxjs';
import {formatIsoDate} from '../util/date-helper';
import {
  TournamentCreateDto, TournamentDetailDto, TournamentDetailParticipantDto,
  TournamentListDto,
  TournamentSearchParams,
  TournamentStandingsDto, TournamentStandingsTreeDto
} from "../dto/tournament";
import {Horse, HorseListDto} from "../dto/horse";
const baseUri = environment.backendUrl + '/tournaments';

class ErrorDto {
  constructor(public message: String) {}
}

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  constructor(
    private http: HttpClient
  ) {
  }

  // \TEMPLATE EXCLUDE END\
  public search(searchParams: TournamentSearchParams): Observable<TournamentListDto[]> {
    let params = new HttpParams();
    if (searchParams.name) {
      params = params.append('name', searchParams.name);
    }
    if (searchParams.startDate) {
      params = params.append('startDate', formatIsoDate(searchParams.startDate));
    }
    if (searchParams.endDate) {
      params = params.append('endDate', formatIsoDate(searchParams.endDate));
    }
    return this.http.get<TournamentListDto[]>(baseUri, { params })
      .pipe(tap(tournaments => tournaments.map(t => {
      t.startDate = new Date(t.startDate);
      t.endDate = new Date(t.endDate);
    })));
  }
  // \TEMPLATE EXCLUDE END\

  /**
   * Create a new tournament in the system.
   *
   * @param tournament the data for the tournament to be created.
   * @return an Observable for the created tournament.
   */
  public create(tournament: TournamentCreateDto): Observable<TournamentDetailDto> {
    return this.http.post<TournamentDetailDto>(
        baseUri,
        tournament
    );
  }

  /**
   * Get the latest standings of a tournament.
   *
   * @param id of the tournament whose standings are wanted.
   * @return an Observable for the standings of the tournament.
   */
  public getStandings (id: number): Observable<TournamentStandingsDto> {
    return this.http.get<TournamentStandingsDto>(
      `${baseUri}/standings/${id}`
    );
  }

  /**
   * Update the standings of a tournament.
   *
   * @param standingsToSet the new standings for the tournament.
   * @param id of the tournament whose standings were changed.
   * @return an Observable of the new tournament standings.
   */
  setStandings(standingsToSet: TournamentStandingsDto, id: number): Observable<TournamentStandingsDto> {
    return this.http.put<TournamentStandingsDto>(
      `${baseUri}/standings/${id}`,
      standingsToSet
    );
  }

  /**
   * Generate the first round of matches for a tournament
   *
   * @param id of the tournament whose first round matches are generated
   * @return an Observable of the new tournament standings
   */
  public generateFirstRound (id: number): Observable<TournamentStandingsDto> {
    return this.http.get<TournamentStandingsDto>(
      `${baseUri}/standings/generate/${id}`
    );
  }
}
