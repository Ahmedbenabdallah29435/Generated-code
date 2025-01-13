import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CommentDTO } from 'app/comment/comment.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CommentService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/comments';

  getAllComments() {
    return this.http.get<CommentDTO[]>(this.resourcePath);
  }

  getComment(id: number) {
    return this.http.get<CommentDTO>(this.resourcePath + '/' + id);
  }

  createComment(commentDTO: CommentDTO) {
    return this.http.post<number>(this.resourcePath, commentDTO);
  }

  updateComment(id: number, commentDTO: CommentDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, commentDTO);
  }

  deleteComment(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/userValues')
        .pipe(map(transformRecordToMap));
  }

  getPostValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/postValues')
        .pipe(map(transformRecordToMap));
  }

}
