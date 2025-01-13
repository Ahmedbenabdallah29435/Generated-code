import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PostReactionService } from 'app/post-reaction/post-reaction.service';
import { PostReactionDTO } from 'app/post-reaction/post-reaction.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validOffsetDateTime } from 'app/common/utils';


@Component({
  selector: 'app-post-reaction-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './post-reaction-edit.component.html'
})
export class PostReactionEditComponent implements OnInit {

  postReactionService = inject(PostReactionService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  userValues?: Map<number,string>;
  postValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    reaction: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    createdAt: new FormControl(null, [validOffsetDateTime]),
    user: new FormControl(null, [Validators.required]),
    post: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@postReaction.update.success:Post Reaction was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.postReactionService.getUserValues()
        .subscribe({
          next: (data) => this.userValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.postReactionService.getPostValues()
        .subscribe({
          next: (data) => this.postValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.postReactionService.getPostReaction(this.currentId!)
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
    const data = new PostReactionDTO(this.editForm.value);
    this.postReactionService.updatePostReaction(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/postReactions'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
