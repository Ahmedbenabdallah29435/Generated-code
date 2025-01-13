import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ReclamationService } from 'app/reclamation/reclamation.service';
import { ReclamationDTO } from 'app/reclamation/reclamation.model';


@Component({
  selector: 'app-reclamation-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './reclamation-list.component.html'})
export class ReclamationListComponent implements OnInit, OnDestroy {

  reclamationService = inject(ReclamationService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  reclamations?: ReclamationDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@reclamation.delete.success:Reclamation was removed successfully.`    };
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
    this.reclamationService.getAllReclamations()
        .subscribe({
          next: (data) => this.reclamations = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.reclamationService.deleteReclamation(id)
          .subscribe({
            next: () => this.router.navigate(['/reclamations'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
