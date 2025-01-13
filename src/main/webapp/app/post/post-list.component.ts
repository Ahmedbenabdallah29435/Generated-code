import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PostService } from 'app/post/post.service';
import { PostDTO } from 'app/post/post.model';


@Component({
  selector: 'app-post-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './post-list.component.html'})
export class PostListComponent implements OnInit, OnDestroy {

  postService = inject(PostService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  posts?: PostDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@post.delete.success:Post was removed successfully.`,
      'post.comment.post.referenced': $localize`:@@post.comment.post.referenced:This entity is still referenced by Comment ${details?.id} via field Post.`,
      'post.postReaction.post.referenced': $localize`:@@post.postReaction.post.referenced:This entity is still referenced by Post Reaction ${details?.id} via field Post.`
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
    this.postService.getAllPosts()
        .subscribe({
          next: (data) => this.posts = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.postService.deletePost(id)
          .subscribe({
            next: () => this.router.navigate(['/posts'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/posts'], {
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
