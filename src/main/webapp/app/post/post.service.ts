import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PostDTO } from 'app/post/post.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PostService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/posts';

  getAllPosts() {
    return this.http.get<PostDTO[]>(this.resourcePath);
  }

  getPost(id: number) {
    return this.http.get<PostDTO>(this.resourcePath + '/' + id);
  }

  createPost(postDTO: PostDTO) {
    return this.http.post<number>(this.resourcePath, postDTO);
  }

  updatePost(id: number, postDTO: PostDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, postDTO);
  }

  deletePost(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/userValues')
        .pipe(map(transformRecordToMap));
  }

}
