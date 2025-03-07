import { signal, Signal, useSignal } from '@vaadin/hilla-react-signals';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import { emptyComponentNameSet } from 'Frontend/components/api/helper';

export interface StateApp {
  isAddNewComponentType: Signal<boolean>,
  isComponentSetUpdated: Signal<boolean>,
  isComponentSetDeleted: Signal<boolean>,
  componentNameInputValue: Signal<string>;
  componentNameValue: Signal<ComponentNameSetDto>;
}

export default function createState() {
  return {
    isAddNewComponentType: signal<boolean>(false),
    isComponentSetDeleted: signal<boolean>(false),
    isComponentSetUpdated: signal<boolean>(false),
    componentNameInputValue: signal<string>(""),
    componentNameValue: signal<ComponentNameSetDto>(emptyComponentNameSet),
  }
}