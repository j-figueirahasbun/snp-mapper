import { Component } from '@angular/core';
import apiService from "./../../services/apiService";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-text-box',
  templateUrl: './text-box.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
  ],
  styleUrls: ['./text-box.component.css'],

})
export class TextBoxComponent {
  inputSNP: string = '';
  snpInfo: string = '' // Define as per the expected response type

  async onClick() {
    try {
      this.snpInfo=''; //clear previous results
      const response = await apiService.getMostLikelyGene(this.inputSNP); // Make the API call
      this.snpInfo = response; // Assuming response.data contains the result
      console.log(response)
    } catch (error) {
      console.error('Error fetching SNP info:', error);
      this.snpInfo = `Error fetching SNP info for ${this.inputSNP}`;
    }
  }
}
