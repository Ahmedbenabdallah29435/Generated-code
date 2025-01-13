export class PostReactionDTO {

  constructor(data:Partial<PostReactionDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  reaction?: string|null;
  createdAt?: string|null;
  user?: number|null;
  post?: number|null;

}
