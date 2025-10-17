import { Component, inject } from '@angular/core';
import { ThemeService } from '../../service/theme-service';
import { LucideAngularModule, Moon, Sun } from 'lucide-angular';

@Component({
  imports: [LucideAngularModule],
  selector: 'app-theme-toggle',
  templateUrl: 'theme-toggle-component.html',
  styleUrls: ['theme-toggle-component.css'],
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
