import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CommentService } from 'app/comment/comment.service';
import { CommentDTO } from 'app/comment/comment.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validOffsetDateTime } from 'app/common/utils';


@Component({
  selector: 'app-comment-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './comment-add.component.html'
})
export class CommentAddComponent implements OnInit {

  commentService = inject(CommentService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  userValues?: Map<number,string>;
  postValues?: Map<number,string>;

  addForm = new FormGroup({
    content: new FormControl(null, [Validators.required]),
    createdAt: new FormControl(null, [validOffsetDateTime]),
    user: new FormControl(null, [Validators.required]),
    post: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@comment.create.success:Comment was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.commentService.getUserValues()
        .subscribe({
          next: (data) => this.userValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.commentService.getPostValues()
        .subscribe({
          next: (data) => this.postValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CommentDTO(this.addForm.value);
    this.commentService.createComment(data)
        .subscribe({
          next: () => this.router.navigate(['/comments'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
