import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../service/user-service';
import { catchError, map, of } from 'rxjs';

export const adminGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  return userService.user$.pipe(
    map((user) => {
      if (user?.roles.includes('ADMIN')) {
        return true;
      }
      return router.createUrlTree(['/']);
    }),
    catchError(() => {
      return of(router.createUrlTree(['/']));
    })
  );
};
