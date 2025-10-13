import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserService } from './service/user-service';
import { ToastComponent } from "./components/toast-component/toast-component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private userSerivce = inject(UserService);
  ngOnInit(): void {
    this.userSerivce.fetchCurrentUser();
  }

  protected readonly title = signal('01Blog');
}
