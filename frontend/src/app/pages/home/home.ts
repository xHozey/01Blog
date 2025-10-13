import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { PostSectionComponent } from '../../components/post-section-component/post-section-component';
import { UserService } from '../../service/user-service';
import { UserSection } from "../../components/user-section/user-section";

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, PostSectionComponent, UserSection],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  user: userResponse | null = null;
  private userService: UserService = inject(UserService);
  ngOnInit() {
    this.userService.user$.subscribe((user) => (this.user = user));
  }
}
