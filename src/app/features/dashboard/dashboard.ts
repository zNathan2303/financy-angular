import { Component } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import { LucideAngularModule, Wallet, CircleArrowUp, CircleArrowDown } from 'lucide-angular';
import { RecentTransactions } from './recent-transactions/recent-transactions';
import { CurrencyPipe } from '@angular/common';
import { TotalPerCategory } from './total-per-category/total-per-category';

@Component({
  selector: 'app-dashboard-page',
  imports: [Header, LucideAngularModule, RecentTransactions, CurrencyPipe, TotalPerCategory],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  readonly Wallet = Wallet;
  readonly CircleArrowUp = CircleArrowUp;
  readonly CircleArrowDown = CircleArrowDown;

  numberFormatBRL = new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 });

  totalBalance = 12_847.32;
  monthlyIncome = 4_250.0;
  monthlyExpense = 2_180.45;

  recentTransactions = [
    {
      id: 1,
      description: 'Pagamento de Salário',
      date: new Date('2025-12-01'),
      category: {
        title: 'Receita',
        color: 'green',
        icon: 'briefcasebusiness',
      },
      income: true,
      value: 4_250.0,
    },
    {
      id: 2,
      description: 'Jantar no Restaurante',
      date: new Date('2025-11-30'),
      category: {
        title: 'Alimentação',
        color: 'blue',
        icon: 'utensils',
      },
      income: false,
      value: 89.5,
    },
    {
      id: 3,
      description: 'Posto de Gasolina',
      date: new Date('2025-11-29'),
      category: {
        title: 'Transporte',
        color: 'purple',
        icon: 'carfront',
      },
      income: false,
      value: 100.0,
    },
    {
      id: 4,
      description: 'Compras no Mercado',
      date: new Date('2025-11-28'),
      category: {
        title: 'Mercado',
        color: 'orange',
        icon: 'shoppingcart',
      },
      income: false,
      value: 156.8,
    },
    {
      id: 5,
      description: 'Retorno de Investimento',
      date: new Date('2025-11-26'),
      category: {
        title: 'Investimento',
        color: 'green',
        icon: 'piggybank',
      },
      income: true,
      value: 340.25,
    },
  ];

  categoryTotalAmount = [
    {
      id: 1,
      category: {
        title: 'Alimentação',
        color: 'blue',
      },
      items: 12,
      totalValue: 542.3,
    },
    {
      id: 2,
      category: {
        title: 'Transporte',
        color: 'purple',
      },
      items: 8,
      totalValue: 385.5,
    },
    {
      id: 3,
      category: {
        title: 'Mercado',
        color: 'orange',
      },
      items: 3,
      totalValue: 298.75,
    },
    {
      id: 4,
      category: {
        title: 'Entretenimento',
        color: 'pink',
      },
      items: 2,
      totalValue: 186.2,
    },
    {
      id: 5,
      category: {
        title: 'Utilidades',
        color: 'yellow',
      },
      items: 7,
      totalValue: 245.8,
    },
  ];
}

const transaction = {
  income: Boolean,
  description: String,
  date: Date,
  value: Number,
  category: Number,
};

const category = {
  title: String,
  description: String,
  icon: String,
  color: String,
};
