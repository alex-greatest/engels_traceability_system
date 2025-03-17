import { signal, Signal, useSignal } from '@vaadin/hilla-react-signals';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import { emptyComponentNameSet } from 'Frontend/components/api/helper';
import dayjs from 'dayjs';

export interface StateApp {
  isAddNewComponentType: Signal<boolean>,
  isComponentSetUpdated: Signal<boolean>,
  isComponentSetDeleted: Signal<boolean>,
  componentNameInputValue: Signal<string>;
  componentNameValue: Signal<ComponentNameSetDto>;
  startDateTimeBoilerOrder: Signal<string>;
  endDateTimeBoilerOrder: Signal<string>;
  startDateTimeBoiler: Signal<string>;
  endDateTimeBoiler: Signal<string>;
}

export default function createState() {
  return {
    isAddNewComponentType: signal<boolean>(false),
    isComponentSetDeleted: signal<boolean>(false),
    isComponentSetUpdated: signal<boolean>(false),
    componentNameInputValue: signal<string>(""),
    componentNameValue: signal<ComponentNameSetDto>(emptyComponentNameSet),
    startDateTimeBoilerOrder: signal<string>(dayjs().startOf('day').format('YYYY-MM-DDTHH:mm:ss')),
    endDateTimeBoilerOrder: signal<string>(dayjs().endOf('day').format('YYYY-MM-DDTHH:mm:ss')),
    startDateTimeBoiler: signal<string>(dayjs().startOf('day').format('YYYY-MM-DDTHH:mm:ss')),
    endDateTimeBoiler: signal<string>(dayjs().endOf('day').format('YYYY-MM-DDTHH:mm:ss'))
  }
}