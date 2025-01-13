import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AuthenticationService } from 'app/security/authentication.service';
import { RegistrationRequest } from 'app/security/authentication.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validOffsetDateTime } from 'app/common/utils';


@Component({
  selector: 'app-registration',
  imports: [CommonModule, ReactiveFormsModule, InputRowComponent],
  templateUrl: './registration.component.html'
})
export class RegistrationComponent {

  authenticationService = inject(AuthenticationService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  registrationForm = new FormGroup({
    username: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(72)]),
    email: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    createdAt: new FormControl(null, [validOffsetDateTime]),
    updatedAt: new FormControl(null, [validOffsetDateTime])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      registrationSuccess: $localize`:@@registration.register.success:Your registration is complete. You may now login.`,
      REGISTRATION_REQUEST_USERNAME_UNIQUE: $localize`:@@registration.register.taken:This account is already taken. Please login.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.registrationForm.markAllAsTouched();
    if (!this.registrationForm.valid) {
      return;
    }
    const data = new RegistrationRequest(this.registrationForm.value);
    this.authenticationService.register(data)
        .subscribe({
          next: () => this.router.navigate(['/login'], {
            state: {
              msgSuccess: this.getMessage('registrationSuccess')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.registrationForm, this.getMessage)
        });
  }

}
