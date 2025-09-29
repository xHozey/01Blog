import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { Router } from '@angular/router';
import { catchError, map, of, tap } from 'rxjs';

export const unauthenticatedGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.getCurrentUser().pipe(
    map((user) => (user ? true : router.createUrlTree(['/login']))),
    catchError(() => of(true))
  );
};
