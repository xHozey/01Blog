import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserService } from './service/user-service';
import { ToastComponent } from "./components/toast-component/toast-component";
import { ThemeService } from './service/theme-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private userSerivce = inject(UserService);
  private themeService = inject(ThemeService);
  ngOnInit(): void {
    this.userSerivce.fetchCurrentUser();

  }
  
  protected readonly title = signal('01Blog');
}
