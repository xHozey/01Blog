import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export const unauthGuardGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.checkAuth().pipe(
    map((isAuthenticated) => {
      if (isAuthenticated) {
        // User is logged in → redirect away from login/register
        return router.createUrlTree(['/'], { queryParams: { returnUrl: state.url } });
      }
      return true; // Allow access
    }),
    catchError(() => {
      // If check fails (e.g., 401), treat as unauthenticated → allow access
      return of(true);
    })
  );
};
