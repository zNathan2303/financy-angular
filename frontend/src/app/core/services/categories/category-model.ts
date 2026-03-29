export interface Category {
  id: number;
  title: string;
  description: string | null;
  color: string;
  icon: string;
  itemsCount: number;
}

export interface CategoryRequest {
  title: string;
  description: string | null;
  color: string;
  icon: string;
}
