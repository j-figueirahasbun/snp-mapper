import {AfterViewInit, Component, ElementRef, ViewChild, viewChild} from '@angular/core';

@Component({
  selector: 'app-chromosome-visual',
  standalone: true,
  imports: [],
  templateUrl: './chromosome-visual.component.html',
  styleUrl: './chromosome-visual.component.css'
})
export class ChromosomeVisualComponent  implements AfterViewInit{
  @ViewChild('chromosomeCanvas') chromosomeCanvas!: ElementRef<HTMLCanvasElement>;

  ngAfterViewInit() {
    const canvas = this.chromosomeCanvas.nativeElement;
    const ctx = canvas.getContext('2d');

    if (ctx) {
      ctx.beginPath();
      ctx.moveTo(50,50);
      ctx.lineTo(450,50);
      ctx.strokeStyle = '#000'
      ctx.lineWidth = 10;
      ctx.stroke()

      const geneStart = 200;
      const geneEnd = 250;
      ctx.fillStyle = 'red';
      ctx.fillRect(geneStart, 45, geneEnd-geneStart, 10)
    }
  }
}
