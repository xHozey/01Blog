import { Component, inject } from '@angular/core';
import { ThemeService } from '../../service/theme-service';
import { LucideAngularModule, Moon, Sun } from 'lucide-angular';

@Component({
  imports: [LucideAngularModule],
  selector: 'app-theme-toggle',
  template: `
    <div class="clickable" (click)="toggleTheme()">
      @if (darkMode) {
      <lucide-icon [img]="DarkIcon"></lucide-icon>
      } @else {
      <lucide-icon [img]="LightIcon"></lucide-icon>
      }
    </div>
  `,
  styles: [
    `
      .clickable {
        cursor: pointer;
      }
    `,
  ],
})
export class ThemeToggleComponent {
  darkMode = false;
  private themeService = inject(ThemeService);
  readonly DarkIcon = Moon;
  readonly LightIcon = Sun;
  constructor() {
    this.themeService.darkMode$.subscribe((isDark) => (this.darkMode = isDark));
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }
}
