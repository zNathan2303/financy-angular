import { Component, inject, OnInit, signal } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import { LucideAngularModule, Wallet, CircleArrowUp, CircleArrowDown } from 'lucide-angular';
import { RecentTransactions } from './recent-transactions/recent-transactions';
import { CurrencyPipe } from '@angular/common';
import { TotalPerCategory } from './total-per-category/total-per-category';
import { DashboardService } from './api/dashboard-service';
import { Category as DashboardCategory, Transaction } from './api/dashboard-model';
import { LoadingService } from '../../shared/services/loading-service';
import { TransactionModalMode } from '../transactions/enums/transaction-modal-mode';
import { ScrollService } from '../../shared/services/scroll-service';
import { Modal } from '../transactions/modal/modal';
import { TransactionRequest } from '../../core/services/transactions/transaction-model';
import { TransactionsService } from '../../core/services/transactions/transactions-service';
import { forkJoin } from 'rxjs';
import { CategoryService } from '../../core/services/categories/category-service';
import { Category } from '../../core/services/categories/category-model';

@Component({
  selector: 'app-dashboard-page',
  imports: [Header, LucideAngularModule, RecentTransactions, CurrencyPipe, TotalPerCategory, Modal],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  readonly Wallet = Wallet;
  readonly CircleArrowUp = CircleArrowUp;
  readonly CircleArrowDown = CircleArrowDown;

  private dashboardService = inject(DashboardService);
  private transactionService = inject(TransactionsService);
  private categoryService = inject(CategoryService);
  private loadingService = inject(LoadingService);
  private scrollService = inject(ScrollService);

  numberFormatBRL = new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 });

  totalBalance = signal(0);
  monthlyIncome = signal(0);
  monthlyExpense = signal(0);
  recentTransactions = signal<Transaction[]>([]);
  categoryTotalAmount = signal<DashboardCategory[]>([]);
  modalState = signal<{
    mode: TransactionModalMode;
  }>({
    mode: TransactionModalMode.CREATE,
  });
  isModalOpen = signal(false);

  categories = signal<Category[]>([]);

  ngOnInit() {
    this.loadingService.show();

    forkJoin({
      dashboard: this.dashboardService.getData(),
      categories: this.categoryService.getAll(),
    }).subscribe({
      next: ({ dashboard, categories }) => {
        this.totalBalance.set(dashboard.totalBalance);
        this.monthlyIncome.set(dashboard.monthlyIncome);
        this.monthlyExpense.set(dashboard.monthlyExpenses);
        this.recentTransactions.set(dashboard.recentTransactions);
        this.categoryTotalAmount.set(dashboard.topCategories);
        this.categories.set(categories);
      },
      error: (err) => {
        console.error(err);
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  createTransaction({ categoryId, date, description, income, value }: TransactionRequest) {
    this.loadingService.show();

    this.transactionService.create({ categoryId, date, description, income, value }).subscribe({
      next: (res) => {
        this.recentTransactions.update((transactions) => {
          const updated = [...transactions, res];

          return updated.sort((a, b) => b.date.localeCompare(a.date));
        });
      },
      error: (err) => {
        alert('Ocorreu um erro ao criar a transação');
        console.error(err);
        this.closeModal();
        this.loadingService.hide();
      },
      complete: () => {
        this.closeModal();
        this.loadingService.hide();
      },
    });
  }

  openModalToCreate() {
    this.modalState.set({
      mode: TransactionModalMode.CREATE,
    });
    this.isModalOpen.set(true);
    this.scrollService.disable();
  }

  closeModal() {
    localStorage.removeItem('transaction-searched');
    this.isModalOpen.set(false);
    this.scrollService.enable();
  }

  handleSubmit(transaction: TransactionRequest | Transaction) {
    const mode = this.modalState().mode;

    if (mode === TransactionModalMode.CREATE) {
      this.createTransaction(transaction as TransactionRequest);
    }
  }
}
