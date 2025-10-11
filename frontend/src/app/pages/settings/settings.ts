import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from "../../components/navbar-component/navbar-component";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './settings.html',
  styleUrls: ['./settings.css'],
})
export class Settings implements OnInit {
  activeTab: 'profile' | 'account' = 'profile';
  
  user = {
    username: 'xHozey',
    email: 'xhozey@example.com',
    bio: 'Just documenting my journey.',
    iconPath: 'assets/default-avatar.png',
  };
  ngOnInit(): void {
    
  }

  selectTab(tab: 'profile' | 'account') {
    this.activeTab = tab;
  }

  onSaveProfile() {
    console.log('Profile saved:', this.user);
  }

  onSaveAccount() {
    console.log('Account settings saved');
  }

  onAvatarChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => (this.user.iconPath = reader.result as string);
      reader.readAsDataURL(file);
    }
  }
}
