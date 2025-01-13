export class CommentReactionDTO {

  constructor(data:Partial<CommentReactionDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  reaction?: string|null;
  createdAt?: string|null;
  user?: number|null;
  comment?: number|null;

}
