import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register-component/register-component';
import { LoginComponent } from './components/login-component/login-component';
import { HomeComponent } from './components/home-component/home-component';
import { unauthenticatedGuard } from './guards/unauthenticated-guard';
import { authenticatedGuard } from './guards/authenticated-guard';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: '', component: HomeComponent, canActivate: [authenticatedGuard] },
];
