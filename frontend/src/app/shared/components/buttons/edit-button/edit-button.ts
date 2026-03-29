import { Component, output } from '@angular/core';
import { LucideAngularModule, SquarePen } from 'lucide-angular';

@Component({
  selector: 'app-edit-button',
  imports: [LucideAngularModule],
  templateUrl: './edit-button.html',
  styleUrl: './edit-button.css',
})
export class EditButton {
  readonly SquarePen = SquarePen;

  clickEvent = output();
}
