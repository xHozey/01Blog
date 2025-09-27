import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, filter, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth-service';

let isRefreshing = false;
let refreshSubject = new BehaviorSubject<boolean>(false);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Skip refresh request
  if (req.url.includes('/refresh')) {
    return next(req);
  }

  const clonedReq = req.clone({ withCredentials: true });
  return next(clonedReq).pipe(
    catchError((error) => {
      if (error.status === 403 || error.status === 401) {
        if (!isRefreshing) {
          isRefreshing = true;
          refreshSubject.next(false);

          return authService.refresh().pipe(
            switchMap(() => {
              isRefreshing = false;
              refreshSubject.next(true);
              return next(req.clone({ withCredentials: true }));
            }),
            catchError((refreshError) => {
              isRefreshing = false;
              router.navigate(['/login']);
              return throwError(() => refreshError);
            })
          );
        } else {
          // Wait for the ongoing refresh to complete
          return refreshSubject.pipe(
            filter((refreshed) => refreshed === true),
            switchMap(() => next(req.clone({ withCredentials: true })))
          );
        }
      }

      return throwError(() => error);
    })
  );
};
