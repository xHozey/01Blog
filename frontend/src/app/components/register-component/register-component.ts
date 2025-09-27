import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Auth } from '../../service/auth';

@Component({
  selector: 'app-register',
  imports: [RouterLink],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css',
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private auth: Auth) {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
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
      console.log('Form is invalid');
      return;
    }
    
    const payload: registerRequest = {
      Username: this.registerForm.value.Username,
      Email: this.registerForm.value.Email,
      Password: this.registerForm.value.Password,
    };

    this.auth.register(payload).subscribe({
      next: (res) => {console.log(res)},
      error: (err) => {console.log(err)},
    });
  }
}
