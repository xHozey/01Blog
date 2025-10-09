import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { FollowService } from '../../service/follow-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {

  private userService = inject(UserService);
  private followService = inject(FollowService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private userId!: number;

  user!: userResponse;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.userId = id ? +id : 0;
    this.userService.getUserById(this.userId).subscribe({
      next: (res) => {
        console.log(res)
        this.user = res;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }


  

}
