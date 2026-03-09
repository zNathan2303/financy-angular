import { Component, computed, signal } from '@angular/core';
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
  ],
  templateUrl: './transactions.html',
  styleUrl: './transactions.css',
})
export class Transactions {
  readonly Plus = Plus;
  readonly Search = Search;
  readonly CircleArrowDown = CircleArrowDown;
  readonly CircleArrowUp = CircleArrowUp;
  readonly Trash = Trash;
  readonly SquarePen = SquarePen;
  readonly ChevronLeft = ChevronLeft;
  readonly ChevronRight = ChevronRight;

  transactions = [
    {
      id: 1,
      description: 'Jantar no Restaurante',
      date: new Date('2025-11-30'),
      categoryId: 1,
      income: false,
      value: 89.5,
    },
    {
      id: 2,
      description: 'Posto de Gasolina',
      date: new Date('2025-11-29'),
      categoryId: 2,
      income: false,
      value: 100,
    },
    {
      id: 3,
      description: 'Compras no Mercado',
      date: new Date('2025-11-28'),
      categoryId: 3,
      income: false,
      value: 156.8,
    },
    {
      id: 4,
      description: 'Retorno de Investimento',
      date: new Date('2025-11-26'),
      categoryId: 4,
      income: true,
      value: 340.25,
    },
    {
      id: 5,
      description: 'Aluguel',
      date: new Date('2025-11-26'),
      categoryId: 5,
      income: false,
      value: 1700,
    },
    {
      id: 6,
      description: 'Freelance',
      date: new Date('2025-11-24'),
      categoryId: 6,
      income: true,
      value: 2500,
    },
    {
      id: 7,
      description: 'Compras Jantar',
      date: new Date('2025-11-22'),
      categoryId: 3,
      income: false,
      value: 150,
    },
    {
      id: 8,
      description: 'Cinema',
      date: new Date('2025-11-18'),
      categoryId: 7,
      income: false,
      value: 88,
    },
    {
      id: 9,
      description: 'Conta de Luz',
      date: new Date('2025-11-17'),
      categoryId: 8,
      income: false,
      value: 220.45,
    },
    {
      id: 10,
      description: 'Farmácia',
      date: new Date('2025-11-16'),
      categoryId: 9,
      income: false,
      value: 67.9,
    },
    {
      id: 11,
      description: 'Ração do Cachorro',
      date: new Date('2025-11-15'),
      categoryId: 10,
      income: false,
      value: 120,
    },
    {
      id: 12,
      description: 'Academia',
      date: new Date('2025-11-14'),
      categoryId: 9,
      income: false,
      value: 99.9,
    },
    {
      id: 13,
      description: 'Compra de Livro',
      date: new Date('2025-11-13'),
      categoryId: 11,
      income: false,
      value: 45,
    },
    {
      id: 14,
      description: 'Viagem',
      date: new Date('2025-11-12'),
      categoryId: 12,
      income: false,
      value: 320,
    },
    {
      id: 15,
      description: 'Presente',
      date: new Date('2025-11-11'),
      categoryId: 13,
      income: false,
      value: 150,
    },
    {
      id: 16,
      description: 'Venda de Produto',
      date: new Date('2025-11-10'),
      categoryId: 6,
      income: true,
      value: 420,
    },
    {
      id: 17,
      description: 'Correios',
      date: new Date('2025-11-09'),
      categoryId: 14,
      income: false,
      value: 35.5,
    },
    {
      id: 18,
      description: 'Almoço Restaurante',
      date: new Date('2025-11-08'),
      categoryId: 1,
      income: false,
      value: 72.3,
    },
    {
      id: 19,
      description: 'Uber',
      date: new Date('2025-11-07'),
      categoryId: 2,
      income: false,
      value: 28,
    },
    {
      id: 20,
      description: 'Mercado da Semana',
      date: new Date('2025-11-06'),
      categoryId: 3,
      income: false,
      value: 210.6,
    },
    {
      id: 21,
      description: 'Dividendos',
      date: new Date('2025-11-05'),
      categoryId: 4,
      income: true,
      value: 95.4,
    },
    {
      id: 22,
      description: 'Cinema com Amigos',
      date: new Date('2025-11-04'),
      categoryId: 7,
      income: false,
      value: 64,
    },
    {
      id: 23,
      description: 'Ferramentas',
      date: new Date('2025-11-03'),
      categoryId: 5,
      income: false,
      value: 180,
    },
    {
      id: 24,
      description: 'Conta de Água',
      date: new Date('2025-11-02'),
      categoryId: 8,
      income: false,
      value: 110.75,
    },
    {
      id: 25,
      description: 'Venda de Item Usado',
      date: new Date('2025-10-31'),
      categoryId: 6,
      income: true,
      value: 250,
    },
    {
      id: 26,
      description: 'Restaurante Japonês',
      date: new Date('2025-10-30'),
      categoryId: 1,
      income: false,
      value: 134.5,
    },
    {
      id: 27,
      description: 'Consulta Médica',
      date: new Date('2025-10-29'),
      categoryId: 9,
      income: false,
      value: 200,
    },
  ];

  searchFormControl = new FormControl({ value: '', disabled: false });
  searchValue = signal('');
  filteredCategory = signal<string | null>(null);
  filteredType = signal<string | null>(null);
  filteredDate = signal<string | null>(null);

  startTransactionsDate = computed(() => {
    let earliestDate = new Date();
    this.transactions.forEach((transaction) => {
      if (transaction.date < earliestDate) {
        earliestDate = transaction.date;
      }
    });

    return earliestDate;
  });
  listMonthsOfTransactions = this.getMonthsFromTheInterval(this.startTransactionsDate());

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

  categories = [
    { id: 1, title: 'Alimentação', color: 'blue', icon: 'utensils' },
    { id: 2, title: 'Transporte', color: 'purple', icon: 'carfront' },
    { id: 3, title: 'Mercado', color: 'orange', icon: 'shoppingcart' },
    { id: 4, title: 'Investimentos', color: 'green', icon: 'piggybank' },
    { id: 5, title: 'Casa', color: 'yellow', icon: 'house' },
    { id: 6, title: 'Trabalho', color: 'green', icon: 'briefcasebusiness' },
    { id: 7, title: 'Entretenimento', color: 'pink', icon: 'ticket' },
    { id: 8, title: 'Contas', color: 'yellow', icon: 'receipttext' },
    { id: 9, title: 'Saúde', color: 'red', icon: 'heartpulse' },
    { id: 10, title: 'Pets', color: 'orange', icon: 'pawprint' },
    { id: 11, title: 'Educação', color: 'blue', icon: 'bookopen' },
    { id: 12, title: 'Viagem', color: 'purple', icon: 'baggageclaim' },
    { id: 13, title: 'Presentes', color: 'pink', icon: 'gift' },
    { id: 14, title: 'Serviços', color: 'gray', icon: 'mailbox' },
  ];

  constructor() {
    this.searchFormControl.valueChanges.subscribe((value) => {
      this.searchValue.set(value ?? '');
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

    return this.transactions.reduce((acc, transaction) => {
      const category = this.categories.find((category) => category.id == transaction.categoryId)!;

      if (searchInputValue != '') {
        if (!transaction.description.toLowerCase().includes(searchInputValue.toLowerCase())) {
          return acc;
        }
      }

      if (typeFilter) {
        if (transaction.income == true && typeFilter != 'income') return acc;
        if (transaction.income == false && typeFilter != 'expense') return acc;
      }

      if (categoryFilter && category.title != categoryFilter) {
        return acc;
      }

      if (dateFilter) {
        const [year, month] = dateFilter.split('-').map((e) => Number(e));

        if (transaction.date.getFullYear() != year || transaction.date.getMonth() != month) {
          return acc;
        }
      }

      acc.push({
        ...transaction,
        category,
      });

      return acc;
    }, [] as transactionDetails[]);
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

interface transactionDetails {
  id: number;
  description: string;
  date: Date;
  categoryId: number;
  income: boolean;
  value: number;
  category: {
    id: number;
    title: string;
    color: string;
    icon: string;
  };
}
