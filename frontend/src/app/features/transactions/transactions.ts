import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import {
  LucideAngularModule,
  Plus,
  Search,
  CircleArrowDown,
  CircleArrowUp,
  LucideIconData,
  Trash,
  SquarePen,
  ChevronLeft,
  ChevronRight,
} from 'lucide-angular';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { FormControl } from '@angular/forms';
import { SelectInput } from '../../shared/components/inputs/select-input/select-input';
import { SelectOption } from '../../shared/components/inputs/select-input/select-option/select-option';
import { CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { CATEGORY_ICONS } from '../../shared/icons/categories';
import { TransactionsService } from '../../core/services/transactions/transactions-service';
import { Transaction } from '../../core/services/transactions/transaction-model';
import { Category } from '../../core/services/categories/category-model';
import { CategoryService } from '../../core/services/categories/category-service';
import { LoadingService } from '../../shared/services/loading-service';
import { forkJoin } from 'rxjs';
import { DeleteButton } from '../../shared/components/buttons/delete-button/delete-button';
import { EditButton } from '../../shared/components/buttons/edit-button/edit-button';

@Component({
  selector: 'app-transactions',
  imports: [
    Header,
    LucideAngularModule,
    InputBase,
    SelectInput,
    SelectOption,
    NgClass,
    DatePipe,
    CurrencyPipe,
    DeleteButton,
    EditButton,
  ],
  templateUrl: './transactions.html',
  styleUrl: './transactions.css',
})
export class Transactions implements OnInit {
  readonly Plus = Plus;
  readonly Search = Search;
  readonly CircleArrowDown = CircleArrowDown;
  readonly CircleArrowUp = CircleArrowUp;
  readonly Trash = Trash;
  readonly SquarePen = SquarePen;
  readonly ChevronLeft = ChevronLeft;
  readonly ChevronRight = ChevronRight;

  private transactionService = inject(TransactionsService);
  private categoryService = inject(CategoryService);
  private loadingService = inject(LoadingService);

  transactions = signal<Transaction[]>([]);
  categories = signal<Category[]>([]);

  ngOnInit() {
    this.loadingService.show();

    forkJoin({
      transactions: this.transactionService.getAll(),
      categories: this.categoryService.getAll(),
    }).subscribe({
      next: ({ transactions, categories }) => {
        this.transactions.set(transactions);
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

  searchFormControl = new FormControl({ value: '', disabled: false });
  searchValue = signal('');
  filteredCategory = signal<string | null>(null);
  filteredType = signal<string | null>(null);
  filteredDate = signal<string | null>(null);

  startTransactionsDate = computed(() => {
    let earliestDate = new Date();
    this.transactions().forEach((transaction) => {
      const [year, month, day] = transaction.date.split('-');
      const date = new Date(Number(year), Number(month) + 1, Number(day));

      if (date < earliestDate) {
        earliestDate = date;
      }
    });

    return earliestDate;
  });
  listMonthsOfTransactions = computed(() =>
    this.getMonthsFromTheInterval(this.startTransactionsDate()),
  );

  currentTablePageNumber = signal(1);
  lastTablePageNumber = computed(() => Math.ceil(this.filteredTransactions().length / 10));
  intervalTablePageNumber = computed(() => {
    const currentPage = this.currentTablePageNumber();
    const lastPage = this.lastTablePageNumber();
    let list: (number | null)[] = [];

    if (lastPage <= 5) {
      for (let countPage = 1; countPage <= lastPage; countPage++) {
        list.push(countPage);
      }

      return list;
    }

    list.push(currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2);

    list = list.filter((page) => (page as number) > 1 && (page as number) < lastPage);

    const hasInitialInterval = (list[0] as number) > 2;
    if (hasInitialInterval) {
      list.unshift(1, null);
    } else {
      list.unshift(1);
    }

    const hasFinalInterval = (list[list.length - 1] as number) + 1 < lastPage;
    if (hasFinalInterval) {
      list.push(null, lastPage);
    } else {
      list.push(lastPage);
    }

    return list;
  });
  currentStartTransaction = computed(() => (this.currentTablePageNumber() - 1) * 10);
  currentEndTransaction = computed(() => {
    const possibleLastTransaction = this.currentStartTransaction() + 10;
    const lastTransaction = this.filteredTransactions().length;

    if (possibleLastTransaction > lastTransaction) {
      return lastTransaction;
    }
    return possibleLastTransaction;
  });

  constructor() {
    this.searchFormControl.valueChanges.subscribe((value) => {
      this.searchValue.set(value ?? '');
      this.currentTablePageNumber.set(1);
    });
  }

  getMonthsFromTheInterval(date: Date) {
    const months = [
      'Janeiro',
      'Fevereiro',
      'Março',
      'Abril',
      'Maio',
      'Junho',
      'Julho',
      'Agosto',
      'Setembro',
      'Outubro',
      'Novembro',
      'Dezembro',
    ];
    const currentDate = new Date();

    if (date > currentDate) return [];

    const startDate = new Date(date);
    const listOfMonths: { id: number; month: string; dateInString: string }[] = [];

    let idCount = 1;

    while (startDate <= currentDate) {
      const month = startDate.getMonth();
      const year = startDate.getFullYear();

      listOfMonths.unshift({
        id: idCount,
        month: months[month] + ' / ' + year,
        dateInString: year + '-' + month,
      });
      idCount++;

      startDate.setMonth(month + 1);
    }

    return listOfMonths;
  }

  filteredTransactions = computed(() => {
    const searchInputValue = this.searchValue();
    const typeFilter = this.filteredType();
    const categoryFilter = this.filteredCategory();
    const dateFilter = this.filteredDate();

    return this.transactions().reduce((acc, transaction) => {
      if (searchInputValue != '') {
        if (!transaction.description.toLowerCase().includes(searchInputValue.toLowerCase())) {
          return acc;
        }
      }

      if (typeFilter) {
        if (transaction.income == true && typeFilter != 'income') return acc;
        if (transaction.income == false && typeFilter != 'expense') return acc;
      }

      if (categoryFilter && transaction.category.title != categoryFilter) {
        return acc;
      }

      if (dateFilter) {
        const [year, month] = dateFilter.split('-').map((e) => Number(e));
        const date = new Date(transaction.date);

        if (date.getFullYear() != year || date.getMonth() != month) {
          return acc;
        }
      }

      acc.push({
        ...transaction,
      });

      return acc;
    }, [] as Transaction[]);
  });

  listTransactions = computed(() => {
    return this.filteredTransactions().slice(
      this.currentStartTransaction(),
      this.currentEndTransaction(),
    );
  });

  changeFilteredCategory(categoryValue: string | null) {
    this.filteredCategory.set(categoryValue || null);
    this.toggleTablePage(1);
  }

  changeFilteredType(typeValue: string | null) {
    this.filteredType.set(typeValue || null);
    this.toggleTablePage(1);
  }

  changeFilteredDate(date: string | null) {
    this.filteredDate.set(date || null);
    this.toggleTablePage(1);
  }

  toggleTablePage(pageNumber: number) {
    this.currentTablePageNumber.set(pageNumber);
  }

  goToPreviousTablePage() {
    this.currentTablePageNumber.update((currentPage) => currentPage - 1);
  }

  goToNextTablePage() {
    this.currentTablePageNumber.update((currentPage) => currentPage + 1);
  }

  getCategoryIcon(name: string): LucideIconData {
    return CATEGORY_ICONS[name as keyof typeof CATEGORY_ICONS];
  }
}
