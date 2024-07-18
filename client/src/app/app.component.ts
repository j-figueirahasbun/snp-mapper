import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {ButtonComponent} from "./button/button.component";
import {TextBoxComponent} from "./text-box/text-box.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ButtonComponent, TextBoxComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'client';
}
