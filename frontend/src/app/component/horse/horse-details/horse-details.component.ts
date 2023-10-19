import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Observable, of, retry} from 'rxjs';
import {Horse} from 'src/app/dto/horse';
import {Sex} from 'src/app/dto/sex';
import {HorseService} from 'src/app/service/horse.service';
import {Breed} from "../../../dto/breed";
import {BreedService} from "../../../service/breed.service";


export enum HorseCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-horse-details',
  templateUrl: './horse-details.component.html',
  styleUrls: ['./horse-details.component.scss']
})
export class HorseDetailsComponent implements OnInit {
  horse: Horse = {
    name: '',
    sex: Sex.female,
    dateOfBirth: new Date(),
    height: 0,
    weight: 0,
  };

  private heightSet: boolean = false;
  private weightSet: boolean = false;
  private dateOfBirthSet: boolean = false;

  get height(): number | null {
    return this.heightSet
        ? this.horse.height
        : null;
  }

  set height(value: number) {
    this.heightSet = true;
    this.horse.height = value;
  }

  get weight(): number | null {
    return this.weightSet
        ? this.horse.weight
        : null;
  }

  set weight(value: number) {
    this.weightSet = true;
    this.horse.weight = value;
  }

  get dateOfBirth(): Date | null {
    return this.dateOfBirthSet
        ? this.horse.dateOfBirth
        : null;
  }
  set dateOfBirth(value: Date) {
    this.dateOfBirthSet = true;
    this.horse.dateOfBirth = value;
  }
  constructor(
      private service: HorseService,
      private breedService: BreedService,
      private router: Router,
      private route: ActivatedRoute,
      private notification: ToastrService,
  ) {
  }
  get sex(): string {
    switch (this.horse.sex) {
      case Sex.male: return 'Male';
      case Sex.female: return 'Female';
      default: return '';
    }
  }
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      let observable: Observable<Horse>;
      let id: number = Number(this.route.snapshot.paramMap.get('id'));
      observable = this.service.getById(id);
      observable.subscribe({
        next: data => {
          this.horse = data;
          this.dateOfBirth = data.dateOfBirth;
          this.weight = data.weight;
          this.height = data.height;
        },
        error: error => {
          console.error('Unknown HorseDetailsError');
          return;
        }
      });
    });
  }

  delete(id: number): void {
    this.service.delete(id).subscribe({
      next: data => {
        this.router.navigate(['/horses']);
      },
      error: error => {
        console.error('Error deleting horse', error);
        if (!!error.error.errors) {
          for (const e of error.error.errors) {
            this.notification.error(e);
          }
        }
        this.notification.error(error.error.message);
      }
    });
  };

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public formatBreedName(breed: Breed | null): string {
    return breed?.name ?? '';
  }

  breedSuggestions = (input: string) => (input === '')
      ? of([])
      :  this.breedService.breedsByName(input, 5);

}
