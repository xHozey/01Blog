import { Component, inject } from '@angular/core';
import { ThemeService } from '../../service/theme-service';

@Component({
  selector: 'app-theme-toggle',
  template: `
    <button (click)="toggleTheme()" class="btn btn-outline-primary">
      {{ darkMode ? '☀️ Light' : '🌙 Dark' }}
    </button>
  `,
})
export class ThemeToggleComponent {
  darkMode = false;
  private themeService = inject(ThemeService);
  constructor() {
    this.themeService.darkMode$.subscribe((isDark) => (this.darkMode = isDark));
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }
}
