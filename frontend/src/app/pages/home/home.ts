import { Component } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { PostSectionComponent } from '../../components/post-section-component/post-section-component';

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, PostSectionComponent],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

}
