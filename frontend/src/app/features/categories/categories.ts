import { Component, computed, inject, signal } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import {
  ArrowUpDown,
  Frown,
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
import { DecimalPipe, NgClass } from '@angular/common';
import { CATEGORY_ICONS } from '../../shared/icons/categories';
import { Modal } from './modal/modal';
import { CategoryModalMode } from './enums/category-modal-mode';
import { DeleteButton } from '../../shared/components/buttons/delete-button/delete-button';
import { EditButton } from '../../shared/components/buttons/edit-button/edit-button';

@Component({
  selector: 'app-categories',
  imports: [Header, LucideAngularModule, NgClass, Modal, DecimalPipe, DeleteButton, EditButton],
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
  readonly Frown = Frown;

  private categoryService = inject(CategoryService);
  private loadingService = inject(LoadingService);

  totalCategories = computed(() => this.categoriesArray().length);
  totalTransactions = computed(() => {
    const transactionsCount = this.categoriesArray().reduce((acc, category) => {
      if (typeof category.itemsCount === 'number') {
        return acc + category.itemsCount;
      }
      return acc;
    }, 0);
    return transactionsCount;
  });
  mostUsedCategory = computed(() => {
    const categories = this.categoriesArray();

    const categoryEmpty: Category = {
      title: 'Não há categorias',
      color: 'gray',
      description: '.',
      icon: '',
      itemsCount: 0,
      id: 0,
    };

    if (categories.length === 0) {
      return categoryEmpty;
    }

    const mostUsedCategory = categories.reduce((acc, category) => {
      return category.itemsCount > acc.itemsCount ? category : acc;
    });
    return mostUsedCategory;
  });

  categoriesArray = signal<Category[]>([]);
  categories = computed(() => {
    const categoriesOrderedByTitle = this.categoriesArray().sort((a, b) =>
      a.title.localeCompare(b.title, 'pt-BR', { sensitivity: 'base' }),
    );
    return categoriesOrderedByTitle;
  });

  isModalOpen = signal(false);

  modalState = signal<{
    mode: CategoryModalMode;
    category?: Category;
  }>({
    mode: CategoryModalMode.CREATE,
  });

  ngOnInit() {
    this.loadingService.show();

    this.categoryService.getAll().subscribe({
      next: (res) => {
        this.categoriesArray.set(res);
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
        this.categoriesArray.update((categories) => [...categories, { ...res, itemsCount: 0 }]);
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

  updateCategory({ color, description, icon, title, id }: Category) {
    this.loadingService.show();

    this.categoryService.update({ color, description, icon, title }, id).subscribe({
      next: (res) => {
        this.categoriesArray.update((categories) =>
          categories.map((c) => (c.id === id ? { ...res, itemsCount: c.itemsCount } : c)),
        );
      },
      error: (err) => {
        alert('Ocorreu um erro ao atualizar a categoria');
        console.error(err);
        this.loadingService.hide();
      },
      complete: () => {
        this.loadingService.hide();
        this.closeModal();
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

  openModalToUpdate(id: number) {
    this.loadingService.show();

    let categorySearched: Category;

    this.categoryService.getbyId(id).subscribe({
      next: (res) => {
        categorySearched = res;
      },
      error: (err) => {
        alert('Ocorreu um erro ao obter dados da categoria');
        console.error(err);
      },
      complete: () => {
        this.modalState.set({
          mode: CategoryModalMode.EDIT,
          category: categorySearched,
        });
        this.loadingService.hide();
        this.isModalOpen.set(true);
      },
    });
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  handleSubmit(category: Category) {
    const mode = this.modalState().mode;

    if (mode === CategoryModalMode.CREATE) {
      this.createCategory(category);
    }

    if (mode === CategoryModalMode.EDIT) {
      this.updateCategory(category);
    }
  }

  getColorVar(color: string) {
    return `var(--${color}-base)`;
  }

  getCategoryIcon(name: string): LucideIconData {
    return CATEGORY_ICONS[name as keyof typeof CATEGORY_ICONS];
  }
}
