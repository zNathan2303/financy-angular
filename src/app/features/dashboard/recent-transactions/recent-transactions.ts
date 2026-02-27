import { Component, input } from '@angular/core';
import {
  LucideAngularModule,
  CircleArrowUp,
  CircleArrowDown,
  ChevronRight,
  LucideIconData,
  Plus,
} from 'lucide-angular';
import { CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { CATEGORY_ICONS } from '../../../shared/icons/categories';

@Component({
  selector: 'app-recent-transactions',
  imports: [LucideAngularModule, NgClass, DatePipe, CurrencyPipe],
  templateUrl: './recent-transactions.html',
  styleUrl: './recent-transactions.css',
})
export class RecentTransactions {
  readonly CircleArrowUp = CircleArrowUp;
  readonly CircleArrowDown = CircleArrowDown;
  readonly ChevronRight = ChevronRight;
  readonly Plus = Plus;

  recentTransactions = input.required<
    Array<{
      id: number;
      description: string;
      date: Date;
      category: {
        title: string;
        color: string;
        icon: string;
      };
      income: boolean;
      value: number;
    }>
  >();

  getCategoryIcon(name: string): LucideIconData {
    return CATEGORY_ICONS[name as keyof typeof CATEGORY_ICONS];
  }
}
