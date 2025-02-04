import React, { Suspense} from 'react';
import {
  TabSheet,
  TabSheetSelectedChangedEvent,
  TabSheetTab
} from '@vaadin/react-components';
import { useSignal } from '@vaadin/hilla-react-signals';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { PropsDialog } from 'Frontend/components/api/helper';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';
import BoilerTypeAdditionalDataSet from 'Frontend/components/boiler_additional_data/BoilerTypeAdditionalDataSet';
import BoilerTypeDataSet from 'Frontend/components/boiler_additional_data/BoilerTypeDataSet';

export default function BoilerTypeDataSetLayout(props: PropsDialog<BoilerTypeAdditionalDataSetDto>) {
  const visitedTabs = useSignal(new Set<number>([0]));

  const selectedTabChanged = (event: TabSheetSelectedChangedEvent) => {
    visitedTabs.value = new Set([...visitedTabs.value, event.detail.value]);
  };

  return (
    <Suspense fallback={"Loading..."}>
      <TabSheet onSelectedChanged={selectedTabChanged}>
        <TabSheetTab label="Работа с данными">
          {visitedTabs.value.has(0) && <BoilerTypeAdditionalDataSet func={props.func} />}
        </TabSheetTab>
        <TabSheetTab label="Создание набора данных">
          {visitedTabs.value.has(1) && <BoilerTypeDataSet />}
        </TabSheetTab>
      </TabSheet>
    </Suspense>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Набор данных котла"
};