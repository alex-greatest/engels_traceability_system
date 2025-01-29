import React, { Suspense} from 'react';
import {
  TabSheet,
  TabSheetSelectedChangedEvent,
  TabSheetTab
} from '@vaadin/react-components';
import { useSignal } from '@vaadin/hilla-react-signals';
import ComponentsSet from 'Frontend/components/component_set/ComponentsSet';
import ComponentsNameSet from 'Frontend/components/component_set/ComponentsNameSet';

export default function SetLayout() {
  const visitedTabs = useSignal(new Set<number>([0]));

  const selectedTabChanged = (event: TabSheetSelectedChangedEvent) => {
    visitedTabs.value = new Set([...visitedTabs.value, event.detail.value]);
  };

  return (
    <Suspense fallback={"Loading..."}>
      <TabSheet onSelectedChanged={selectedTabChanged}>
        <TabSheetTab label="Создание компонетнов">
          {visitedTabs.value.has(0) && <ComponentsSet />}
        </TabSheetTab>
        <TabSheetTab label="Создание набора компонентов">
          {visitedTabs.value.has(1) && <ComponentsNameSet />}
        </TabSheetTab>
      </TabSheet>
    </Suspense>
  );
}