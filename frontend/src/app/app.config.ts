import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { Image, LucideAngularModule } from 'lucide-angular';
export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(LucideAngularModule.pick({Image})),
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes)
  ]
};
