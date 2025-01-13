export class PostDTO {

  constructor(data:Partial<PostDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  content?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  user?: number|null;

}
