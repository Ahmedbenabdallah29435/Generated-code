import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CommentReactionDTO } from 'app/comment-reaction/comment-reaction.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CommentReactionService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/commentReactions';

  getAllCommentReactions() {
    return this.http.get<CommentReactionDTO[]>(this.resourcePath);
  }

  getCommentReaction(id: number) {
    return this.http.get<CommentReactionDTO>(this.resourcePath + '/' + id);
  }

  createCommentReaction(commentReactionDTO: CommentReactionDTO) {
    return this.http.post<number>(this.resourcePath, commentReactionDTO);
  }

  updateCommentReaction(id: number, commentReactionDTO: CommentReactionDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, commentReactionDTO);
  }

  deleteCommentReaction(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/userValues')
        .pipe(map(transformRecordToMap));
  }

  getCommentValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/commentValues')
        .pipe(map(transformRecordToMap));
  }

}
