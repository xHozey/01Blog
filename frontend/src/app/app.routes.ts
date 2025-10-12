import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { CreatePost } from './pages/create-post/create-post';
import { Post } from './pages/post/post';
import { UpdatePost } from './pages/update-post/update-post';
import { postEditGuard } from './guards/post-edit-guard-guard';
import { Profile } from './pages/profile/profile';
import { Settings } from './pages/settings/settings';
import { AdminDashboard } from './pages/admin-dashboard/admin-dashboard';
import { authGuard } from './guards/auth-guard-guard';
import { unauthGuardGuard } from './guards/unauth-guard-guard';
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [
  {
    path: '',
    component: Home,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [unauthGuardGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [unauthGuardGuard],
  },
  {
    path: 'create-post',
    component: CreatePost,
    canActivate: [authGuard],
  },
  {
    path: 'post/:id',
    component: Post,
    canActivate: [authGuard],
  },
  {
    path: 'edit/:id',
    component: UpdatePost,
    canActivate: [postEditGuard, authGuard],
  },
  {
    path: 'profile/:id',
    component: Profile,
    canActivate: [authGuard],
  },
  {
    path: 'settings',
    component: Settings,
    canActivate: [authGuard],
  },
  {
    path: 'admin/dashboard',
    component: AdminDashboard,
    canActivate: [authGuard, adminGuard],
  },
];
