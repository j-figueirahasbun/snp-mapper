import { Component } from '@angular/core';
import apiService from './../../services/apiService';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {
  async onClick() {
    try {
      const text = await apiService.getText();
      console.log('Response:', text); // Output the response to console, adjust as needed
    } catch (error) {
      console.error('Error:', error);
    }
  }
}
