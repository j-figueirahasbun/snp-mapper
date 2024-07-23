import { Component } from '@angular/core';
import apiService from "./../../services/apiService";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common"; // Adjust import as per your service definition

@Component({
  selector: 'app-text-box',
  // @ts-ignore
  // imports: [apiService, ],
  templateUrl: './text-box.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  styleUrls: ['./text-box.component.css']
})
export class TextBoxComponent {
  inputSNP: string = '';
  snpInfo: string = ' ' // Define as per the expected response type

  // constructor(private apiService) {} // Inject ApiService

  async onClick() {
    try {
      const response = await apiService.getSNPInfo(this.inputSNP); // Make the API call
      this.snpInfo = response; // Assuming response.data contains the result
      console.log(response)
    } catch (error) {
      console.error('Error fetching SNP info:', error);
      this.snpInfo = `Error fetching SNP info for ${this.inputSNP}`;
    }
  }
}
