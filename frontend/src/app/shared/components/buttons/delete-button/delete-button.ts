import { Component, output } from '@angular/core';
import { LucideAngularModule, Trash } from 'lucide-angular';

@Component({
  selector: 'app-delete-button',
  imports: [LucideAngularModule],
  templateUrl: './delete-button.html',
  styleUrl: './delete-button.css',
})
export class DeleteButton {
  readonly Trash = Trash;

  clickEvent = output();
}
