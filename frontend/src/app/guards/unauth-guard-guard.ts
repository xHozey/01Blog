import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export const unauthGuardGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.checkUnauth().pipe(
    map((canAccess) => {
      if (!canAccess) {
        return router.createUrlTree(['/']);
      }
      return true;
    }),
    catchError(() => {
      return of(true);
    })
  );
};
