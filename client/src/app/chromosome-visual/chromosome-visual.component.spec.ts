import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChromosomeVisualComponent } from './chromosome-visual.component';

describe('ChromosomeVisualComponent', () => {
  let component: ChromosomeVisualComponent;
  let fixture: ComponentFixture<ChromosomeVisualComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChromosomeVisualComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChromosomeVisualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
