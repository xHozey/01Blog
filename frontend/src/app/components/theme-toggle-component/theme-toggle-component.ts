import { Component, inject } from '@angular/core';
import { ThemeService } from '../../service/theme-service';

@Component({
  selector: 'app-theme-toggle',
  template: `
    <div class="form-check form-switch">
      <input
        class="form-check-input"
        type="checkbox"
        id="themeSwitch"
        [checked]="darkMode"
        (change)="toggleTheme()"
      />
      <label class="form-check-label" for="themeSwitch">
        {{ darkMode ? '🌙' : '☀️' }}
      </label>
    </div>
  `,
  styles: [
    `
      .form-switch .form-check-input {
        cursor: pointer;
      }
    `,
  ],
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
