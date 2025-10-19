import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const apiErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const isLoginOrRefresh =
    req.url.includes('/refresh') ||
    req.url.includes('/login') ||
    req.url.includes('/register') ||
    req.url.includes('/check-unauth');

  return next(req).pipe(
    catchError((err) => {
      if (err.status == 404 && !isLoginOrRefresh) {
        router.navigate(['not-found']);
      }
      return throwError(() => err);
    })
  );
};
