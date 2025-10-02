import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { PostSectionComponent } from '../../components/post-section-component/post-section-component';
import { UserService } from '../../service/user-service';

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, PostSectionComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  user: userResponse | null = null;
  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.fetchCurrentUser();
    this.userService.user$.subscribe((user) => (this.user = user));
  }
}
