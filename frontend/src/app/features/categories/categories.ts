import { Component, computed, inject, signal } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import {
  ArrowUpDown,
  LucideAngularModule,
  LucideIconData,
  Plus,
  SquarePen,
  Tag,
  Trash,
  Utensils,
} from 'lucide-angular';
import { LoadingService } from '../../shared/services/loading-service';
import { CategoryService } from '../../core/services/categories/category-service';
import { Category, CategoryRequest } from '../../core/services/categories/category-model';
import { NgClass } from '@angular/common';
import { CATEGORY_ICONS } from '../../shared/icons/categories';
import { Modal } from './modal/modal';
import { CategoryModalMode } from './enums/category-modal-mode';

@Component({
  selector: 'app-categories',
  imports: [Header, LucideAngularModule, NgClass, Modal],
  templateUrl: './categories.html',
  styleUrl: './categories.css',
})
export class Categories {
  readonly Tag = Tag;
  readonly ArrowUpDown = ArrowUpDown;
  readonly Utensils = Utensils;
  readonly Plus = Plus;
  readonly Trash = Trash;
  readonly SquarePen = SquarePen;

  private categoryService = inject(CategoryService);
  private loadingService = inject(LoadingService);

  totalCategories = signal(0);
  totalTransactions = signal(0);
  mostUsedCategoryTitle = signal('.');
  categoriesArray = signal<Category[]>([]);
  categories = computed(() => {
    return this.categoriesArray().sort((a, b) =>
      a.title.localeCompare(b.title, 'pt-BR', { sensitivity: 'base' }),
    );
  });

  isModalOpen = signal(false);

  modalState = signal<{
    mode: CategoryModalMode;
    category?: CategoryRequest;
  }>({
    mode: CategoryModalMode.CREATE,
  });

  ngOnInit() {
    this.loadingService.show();

    this.categoryService.getAll().subscribe({
      next: (res) => {
        this.categoriesArray.set(res);

        this.totalCategories.set(res.length);

        const transactionsCount = res.reduce((acc, category) => {
          return acc + category.itemsCount;
        }, 0);
        this.totalTransactions.set(transactionsCount);

        const mostUsedCategory = res.reduce((acc, category) => {
          return category.itemsCount > acc.itemsCount ? category : acc;
        });
        this.mostUsedCategoryTitle.set(mostUsedCategory.title);
      },
      error: (err) => {
        alert('Ocorreu um erro ao carregar as categorias');
        console.error(err);
        this.loadingService.hide();
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  createCategory({ color, description, icon, title }: CategoryRequest) {
    this.loadingService.show();

    this.categoryService.create({ color, description, icon, title }).subscribe({
      next: (res) => {
        this.categoriesArray.update((categories) => [...categories, res]);
      },
      error: (err) => {
        alert('Ocorreu um erro ao criar a categoria');
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

  deleteCategory(id: number) {
    this.loadingService.show();

    this.categoryService.deleteById(id).subscribe({
      next: (res) => {
        this.categoriesArray.update((categories) => categories.filter((c) => c.id !== id));
      },
      error: (err) => {
        alert('Ocorreu um erro ao excluir a categoria');
        console.error(err);
        this.loadingService.hide();
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  openModalToCreate() {
    this.modalState.set({
      mode: CategoryModalMode.CREATE,
      category: undefined,
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  handleSubmit({ color, description, icon, title }: CategoryRequest) {
    const mode = this.modalState()?.mode;

    if (mode === CategoryModalMode.CREATE) {
      this.createCategory({ color, description, icon, title });
    }
  }

  getCategoryIcon(name: string): LucideIconData {
    return CATEGORY_ICONS[name as keyof typeof CATEGORY_ICONS];
  }
}
