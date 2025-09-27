import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { Router } from '@angular/router';
import { catchError, map, of, tap } from 'rxjs';

export const unauthenticatedGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.getCurrentUser().pipe(
    tap(user => {
      if (user) router.navigate(['/']);
    }),
    map(user => !user),
    catchError(() => of(true))
  );
};

