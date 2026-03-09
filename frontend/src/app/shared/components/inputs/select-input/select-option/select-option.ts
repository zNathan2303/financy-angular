import { Component, ElementRef, inject, input, ViewChild } from '@angular/core';
import { Check, LucideAngularModule } from 'lucide-angular';
import { SelectInput } from '../select-input';

@Component({
  selector: 'app-select-option',
  imports: [LucideAngularModule],
  styleUrl: './select-option.css',
  template: `<input
      #option
      type="radio"
      [name]="select?.name()"
      [value]="value()"
      (click)="onClick($event)"
      [checked]="isChecked()"
    />

    <div>
      <ng-content />
    </div>

    <lucide-icon [img]="Check" size="20" /> `,
})
export class SelectOption {
  readonly Check = Check;

  select = inject<SelectInput>(SelectInput, { optional: true });
  thisComponent = inject(ElementRef<HTMLElement>);

  value = input.required<string | null>();
  optionName = input.required<string>();

  ngOnInit() {
    this.select?.registerOption(this);
  }

  onClick(event: PointerEvent) {
    this.select?.selectOptionName(this.optionName(), this.value());

    const isMouseOrTouch = event.pointerType == 'mouse' || event.pointerType == 'touch';

    if (isMouseOrTouch) {
      this.select?.toggleMenuState();
    }
  }

  @ViewChild('option', { static: true })
  inputRef!: ElementRef<HTMLInputElement>;

  focus() {
    this.inputRef.nativeElement.focus();
  }

  isChecked() {
    return this.inputRef.nativeElement.checked;
  }
}
