export class ReclamationDTO {

  constructor(data:Partial<ReclamationDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  status?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  user?: number|null;

}
