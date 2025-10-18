import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../service/auth-service';
import { RefreshService } from '../service/refresh-service';
import { catchError, switchMap, throwError, Subject, Observable } from 'rxjs';
import { Router } from '@angular/router';

let isRefreshing = false;
const refreshSubject = new Subject<void>();

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const refreshService = inject(RefreshService);
  const authService = inject(AuthService);
  const router = inject(Router);
  const clonedReq = req.clone({ withCredentials: true });

  const handleRequest = (): Observable<HttpEvent<unknown>> =>
    next(clonedReq).pipe(
      catchError((err: HttpErrorResponse) => {
        const isLoginOrRefresh =
          req.url.includes('/refresh') ||
          req.url.includes('/login') ||
          req.url.includes('/register') ||
          req.url.includes('/check-unauth');
        if (err.status === 401 && !isLoginOrRefresh) {
          if (!isRefreshing) {
            isRefreshing = true;

            return refreshService.refresh().pipe(
              switchMap(() => {
                isRefreshing = false;
                refreshSubject.next();
                return next(clonedReq);
              }),
              catchError((refreshErr) => {
                isRefreshing = false;

                if (refreshErr.status === 401 || refreshErr.status === 403) {
                  authService.logout().subscribe({
                    next: () => router.navigate(['/login']),
                    error: () => {},
                  });
                }

                return throwError(() => refreshErr);
              })
            );
          } else {
            return new Observable<HttpEvent<unknown>>((observer) => {
              const subscription = refreshSubject.subscribe({
                next: () => {
                  next(clonedReq).subscribe({
                    next: (result) => observer.next(result),
                    error: (err) => observer.error(err),
                    complete: () => observer.complete(),
                  });
                },
                error: (err) => {
                  observer.error(err);
                },
              });
              return () => subscription.unsubscribe();
            });
          }
        }

        return throwError(() => err);
      })
    );

  return handleRequest();
};
