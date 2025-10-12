import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../service/user-service';
import { map } from 'rxjs';

export const adminGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  return userService.user$.pipe(
    map((user) => {
      if (user?.roles.includes('ADMIN')) {
        return true;
      }
      return router.createUrlTree(['/']);
    })
  );
};
