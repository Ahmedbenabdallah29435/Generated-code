import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ReclamationDTO } from 'app/reclamation/reclamation.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ReclamationService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/reclamations';

  getAllReclamations() {
    return this.http.get<ReclamationDTO[]>(this.resourcePath);
  }

  getReclamation(id: number) {
    return this.http.get<ReclamationDTO>(this.resourcePath + '/' + id);
  }

  createReclamation(reclamationDTO: ReclamationDTO) {
    return this.http.post<number>(this.resourcePath, reclamationDTO);
  }

  updateReclamation(id: number, reclamationDTO: ReclamationDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, reclamationDTO);
  }

  deleteReclamation(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/userValues')
        .pipe(map(transformRecordToMap));
  }

}
