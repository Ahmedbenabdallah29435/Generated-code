import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CommentReactionService } from 'app/comment-reaction/comment-reaction.service';
import { CommentReactionDTO } from 'app/comment-reaction/comment-reaction.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validOffsetDateTime } from 'app/common/utils';


@Component({
  selector: 'app-comment-reaction-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './comment-reaction-add.component.html'
})
export class CommentReactionAddComponent implements OnInit {

  commentReactionService = inject(CommentReactionService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  userValues?: Map<number,string>;
  commentValues?: Map<number,string>;

  addForm = new FormGroup({
    reaction: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    createdAt: new FormControl(null, [validOffsetDateTime]),
    user: new FormControl(null, [Validators.required]),
    comment: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@commentReaction.create.success:Comment Reaction was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.commentReactionService.getUserValues()
        .subscribe({
          next: (data) => this.userValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.commentReactionService.getCommentValues()
        .subscribe({
          next: (data) => this.commentValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CommentReactionDTO(this.addForm.value);
    this.commentReactionService.createCommentReaction(data)
        .subscribe({
          next: () => this.router.navigate(['/commentReactions'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
