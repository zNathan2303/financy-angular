import { Component, input } from '@angular/core';
import { LucideAngularModule, ChevronRight } from 'lucide-angular';
import { NgClass, CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-total-per-category',
  imports: [LucideAngularModule, NgClass, CurrencyPipe],
  templateUrl: './total-per-category.html',
  styleUrl: './total-per-category.css',
})
export class TotalPerCategory {
  readonly ChevronRight = ChevronRight;

  categoryTotalAmount = input.required<
    Array<{
      id: number;
      category: {
        title: string;
        color: string;
      };
      items: number;
      totalValue: number;
    }>
  >();
}
