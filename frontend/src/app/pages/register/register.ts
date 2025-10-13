import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  registerForm!: FormGroup;

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(200)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(64)]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      Object.keys(this.registerForm.controls).forEach((key) => {
        const control = this.registerForm.get(key);
        if (control && control.errors) {
          for (const errorKey in control.errors) {
            switch (errorKey) {
              case 'required':
                this.toastService.error(`${key} is required`);
                break;
              case 'minlength':
                this.toastService.error(
                  `${key} must be at least ${control.errors['minlength'].requiredLength} characters`
                );
                break;
              case 'maxlength':
                this.toastService.error(
                  `${key} cannot exceed ${control.errors['maxlength'].requiredLength} characters`
                );
                break;
              case 'email':
                this.toastService.error(`Invalid email format`);
                break;
            }
          }
        }
      });
      return;
    }
    if (this.registerForm.value.password != this.registerForm.value.confirmPassword) {
      this.toastService.error('mismatched password');
      return;
    }
    const payload: userRequest = {
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
    };

    this.authService.register(payload).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
  }
}
