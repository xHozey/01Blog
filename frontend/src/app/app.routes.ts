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

export const routes: Routes = [
  {
    path: '',
    component: Home,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'create-post',
    component: CreatePost,
  },
  {
    path: 'post/:id',
    component: Post,
  },
  {
    path: 'edit/:id',
    component: UpdatePost,
    canActivate: [postEditGuard],
  },
  {
    path: 'profile/:id',
    component: Profile,
  },
  {
    path: "settings",
    component: Settings
  }
];
