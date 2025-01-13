import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CommentReactionService } from 'app/comment-reaction/comment-reaction.service';
import { CommentReactionDTO } from 'app/comment-reaction/comment-reaction.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validOffsetDateTime } from 'app/common/utils';


@Component({
  selector: 'app-comment-reaction-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './comment-reaction-edit.component.html'
})
export class CommentReactionEditComponent implements OnInit {

  commentReactionService = inject(CommentReactionService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  userValues?: Map<number,string>;
  commentValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    reaction: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    createdAt: new FormControl(null, [validOffsetDateTime]),
    user: new FormControl(null, [Validators.required]),
    comment: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@commentReaction.update.success:Comment Reaction was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.commentReactionService.getCommentReaction(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new CommentReactionDTO(this.editForm.value);
    this.commentReactionService.updateCommentReaction(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/commentReactions'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
