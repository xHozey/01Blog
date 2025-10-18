import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map, switchMap, of, catchError } from 'rxjs';
import { PostService } from '../service/post-service';
import { UserService } from '../service/user-service';

export const postEditGuard: CanActivateFn = (route, state) => {
  const postService = inject(PostService);
  const userService = inject(UserService);
  const router = inject(Router);

  const postId = route.params['id'];

  return postService.getPost(postId).pipe(
    switchMap((post) => {
      const currentUser = userService.getUser();

      if (currentUser) {
        if (currentUser.id === post.authorId) return of(true);
        return of(router.createUrlTree(['/']));
      }

      return userService.user$.pipe(
        map((user) => {
          if (user && user.id === post.authorId) return true;
          return router.createUrlTree(['/']);
        }),
        catchError(() => {
          return of(router.createUrlTree(['/']));
        })
      );
    }),
    catchError(() => {
      return of(router.createUrlTree(['/']));
    })
  );
};
