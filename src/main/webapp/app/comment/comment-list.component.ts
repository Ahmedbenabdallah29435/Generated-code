import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CommentService } from 'app/comment/comment.service';
import { CommentDTO } from 'app/comment/comment.model';


@Component({
  selector: 'app-comment-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './comment-list.component.html'})
export class CommentListComponent implements OnInit, OnDestroy {

  commentService = inject(CommentService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  comments?: CommentDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@comment.delete.success:Comment was removed successfully.`,
      'comment.commentReaction.comment.referenced': $localize`:@@comment.commentReaction.comment.referenced:This entity is still referenced by Comment Reaction ${details?.id} via field Comment.`
    };
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
    this.commentService.getAllComments()
        .subscribe({
          next: (data) => this.comments = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.commentService.deleteComment(id)
          .subscribe({
            next: () => this.router.navigate(['/comments'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/comments'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
