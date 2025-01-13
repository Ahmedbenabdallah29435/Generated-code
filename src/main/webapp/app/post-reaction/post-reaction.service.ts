import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PostReactionDTO } from 'app/post-reaction/post-reaction.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PostReactionService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/postReactions';

  getAllPostReactions() {
    return this.http.get<PostReactionDTO[]>(this.resourcePath);
  }

  getPostReaction(id: number) {
    return this.http.get<PostReactionDTO>(this.resourcePath + '/' + id);
  }

  createPostReaction(postReactionDTO: PostReactionDTO) {
    return this.http.post<number>(this.resourcePath, postReactionDTO);
  }

  updatePostReaction(id: number, postReactionDTO: PostReactionDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, postReactionDTO);
  }

  deletePostReaction(id: number) {
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
