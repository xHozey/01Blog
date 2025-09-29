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

  if (req.url.includes('/refresh') || req.url.includes('/login') || req.url.includes('/register')) {
    return next(req);
  }

  const clonedReq = req.clone({ withCredentials: true });
  return next(clonedReq).pipe(
    catchError((error) => {
      if (error.status === 401) {
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
              console.log(error);
              return throwError(() => {
                refreshError;
              });
            })
          );
        } else {
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
