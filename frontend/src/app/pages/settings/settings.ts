import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { UserService } from '../../service/user-service';
import { MediaService } from '../../service/media-service';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './settings.html',
  styleUrls: ['./settings.css'],
})
export class Settings implements OnInit {
  private userService = inject(UserService);
  private mediaService = inject(MediaService);
  private toastService = inject(ToastService);

  activeTab: 'profile' | 'account' = 'profile';

  user: userResponse | null = null;
  accountPayload: userAccountUpdateRequest = { email: null, oldPassword: null, newPassword: null };
  profilePayload: userProfileUpdateRequest = { username: null, bio: null, iconProfile: null };

  ngOnInit(): void {
    this.userService.user$.subscribe((user) => {
      this.user = user;
      this.profilePayload.bio = this.user?.bio || null;
      this.profilePayload.username = this.user?.username || null;
      this.profilePayload.iconProfile = this.user?.iconPath || null;
    });
  }

  selectTab(tab: 'profile' | 'account') {
    this.activeTab = tab;
  }

  onSaveProfile() {
    this.userService.updateUserProfile(this.profilePayload).subscribe({
      next: (res) => {},
      error: (err) => {
                parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onSaveAccount() {
    this.userService.updateUserAccount(this.accountPayload).subscribe({
      next: (res) => {},
      error: (err) => {
                parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onAvatarChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const form = new FormData();
      form.append('file', file);
      this.mediaService.addUserIcon(form).subscribe({
        next: (res) => {
          this.profilePayload.iconProfile = res;
        },
        error: (err) => {
                  parseApiError(err).forEach((msg) => this.toastService.error(msg));
        },
      });
    }
  }
}
