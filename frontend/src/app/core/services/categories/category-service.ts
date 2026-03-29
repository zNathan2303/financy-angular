import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Category, CategoryRequest } from './category-model';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private readonly pathCategories = '/financy/v1/categories';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Category[]>(this.pathCategories);
  }

  create({ title, description, color, icon }: CategoryRequest) {
    return this.http.post<Category>(this.pathCategories, {
      title,
      description,
      color,
      icon,
    });
  }

  deleteById(id: number) {
    return this.http.delete(`${this.pathCategories}/${id}`);
  }

  getbyId(id: number) {
    return this.http.get<Category>(`${this.pathCategories}/${id}`);
  }

  update({ title, description, color, icon }: CategoryRequest, id: number) {
    return this.http.put<Category>(`${this.pathCategories}/${id}`, {
      title,
      description,
      color,
      icon,
    });
  }
}
