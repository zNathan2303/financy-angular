import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalPerCategory } from './total-per-category';

describe('TotalPerCategory', () => {
  let component: TotalPerCategory;
  let fixture: ComponentFixture<TotalPerCategory>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TotalPerCategory]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalPerCategory);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
