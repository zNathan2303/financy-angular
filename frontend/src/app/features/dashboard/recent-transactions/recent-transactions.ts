import { Component, input, output } from '@angular/core';
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
import { RouterLink } from '@angular/router';
import { Transaction } from '../api/dashboard-model';

@Component({
  selector: 'app-recent-transactions',
  imports: [LucideAngularModule, NgClass, DatePipe, CurrencyPipe, RouterLink],
  templateUrl: './recent-transactions.html',
  styleUrl: './recent-transactions.css',
})
export class RecentTransactions {
  readonly CircleArrowUp = CircleArrowUp;
  readonly CircleArrowDown = CircleArrowDown;
  readonly ChevronRight = ChevronRight;
  readonly Plus = Plus;

  newTransactionClickEvent = output();

  recentTransactions = input.required<Array<Transaction>>();

  getCategoryIcon(name: string): LucideIconData {
    return CATEGORY_ICONS[name as keyof typeof CATEGORY_ICONS];
  }
}
