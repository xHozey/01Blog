import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { map, catchError, of, tap } from 'rxjs';


export const authenticatedGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.getCurrentUser().pipe(
    tap(user => {
      if (!user) router.navigate(['/login']);
    }),
    map(user => !!user),
    catchError(() => {
      router.navigate(['/login']);
      return of(false);
    })
  );
};

