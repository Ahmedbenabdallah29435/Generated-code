import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CommentReactionService } from 'app/comment-reaction/comment-reaction.service';
import { CommentReactionDTO } from 'app/comment-reaction/comment-reaction.model';


@Component({
  selector: 'app-comment-reaction-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './comment-reaction-list.component.html'})
export class CommentReactionListComponent implements OnInit, OnDestroy {

  commentReactionService = inject(CommentReactionService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  commentReactions?: CommentReactionDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@commentReaction.delete.success:Comment Reaction was removed successfully.`    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.commentReactionService.getAllCommentReactions()
        .subscribe({
          next: (data) => this.commentReactions = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.commentReactionService.deleteCommentReaction(id)
          .subscribe({
            next: () => this.router.navigate(['/commentReactions'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
