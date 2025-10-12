import { Component, inject } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login-component',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  loginForm: FormGroup;
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  constructor() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(64)]],
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach((key) => {
        const controlErrors = this.loginForm.get(key)?.errors;
        if (controlErrors) {
          console.log(`Validation errors in '${key}':`, controlErrors);
        }
      });
    }
    const payload: userRequest = {
      email: this.loginForm.value.username,
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
    };

    this.auth.login(payload).subscribe({
      next: (res) => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
}
