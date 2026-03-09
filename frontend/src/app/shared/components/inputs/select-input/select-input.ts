import {
  Component,
  computed,
  effect,
  ElementRef,
  HostListener,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import {
  LucideAngularModule,
  ChevronUp,
  ChevronDown,
  Check,
  CircleArrowUp,
  CircleArrowDown,
} from 'lucide-angular';
import { SelectOption } from './select-option/select-option';

@Component({
  selector: 'app-select-input',
  imports: [LucideAngularModule],
  templateUrl: './select-input.html',
  styleUrl: './select-input.css',
})
export class SelectInput {
  readonly Check = Check;
  readonly CircleArrowUp = CircleArrowUp;
  readonly CircleArrowDown = CircleArrowDown;
  ChevronIcon = computed(() => (this.isOpen() ? ChevronUp : ChevronDown));

  private thisComponent = inject(ElementRef<HTMLElement>);
  private options: SelectOption[] = [];

  fieldName = 'select-type';
  placeholder = input.required<string>();
  label = input.required<string>();
  name = input.required<string>();

  selectedOptionName = signal<string | null>(null);
  displayValue = computed(() => this.selectedOptionName() ?? this.placeholder());
  viewSelectedValue = output<string | null>();

  isOpen = signal(false);

  // Após abrir o menu focar a opção selecionada ou a primeira opção
  constructor() {
    effect(() => {
      if (!this.isOpen()) return;

      queueMicrotask(() => {
        const options = this.options;

        const selected = options.find((o) => o.isChecked()) ?? options[0];

        selected?.focus();
      });
    });
  }

  @HostListener('keydown', ['$event'])
  exitMenuWithKeys(event: KeyboardEvent) {
    if (!this.isOpen()) return;

    if (event.key == 'Escape' || event.key == ' ' || event.key == 'Enter') {
      this.isOpen.set(false);
      return;
    }
  }

  @HostListener('document:pointerdown', ['$event'])
  exitMenuWithClick(event: MouseEvent) {
    if (!this.isOpen()) return;

    const target = event.target as Node;

    const clickedHost = this.thisComponent.nativeElement.contains(target);

    if (!clickedHost) {
      this.isOpen.set(false);
    }
  }

  registerOption(option: SelectOption) {
    this.options.push(option);
  }

  selectOptionName(optionName: string, value: string | null) {
    this.selectedOptionName.set(optionName);
    this.viewSelectedValue.emit(value);
  }

  toggleMenuState() {
    this.isOpen.set(!this.isOpen());
  }

  isSelected = (optionName: string): boolean => {
    return this.selectedOptionName() == optionName;
  };
}
