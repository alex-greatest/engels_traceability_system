import { Signal, useSignal } from '@vaadin/hilla-react-signals';
import { useComponentSet } from 'Frontend/components/api/components_set';
import React from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack
} from '@mui/material';
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import { emptyComponentNameSet } from 'Frontend/components/api/helper';
import CheckIcon from '@mui/icons-material/Check';
import SetLayout from 'Frontend/views/components/set/@layout';

interface Props {
  dialogOpen: Signal<boolean>;
  updaterComponentNameSet: Signal<(componentNameSet: ComponentNameSetDto) => void>;
}

const ComponentsSetDialog = (props: Props) => {
  const { dialogOpen, updaterComponentNameSet } = props;
  const { data: componentsNameSet, isLoading: isLoadingComponentsNameSet,
    isError:  isErrorComponentsNameSet, isRefetching: isRefetchingComponentsNameSet} = useComponentsNameSet();
  const componentNameInputValue = useSignal<string>("");
  const componentNameValue = useSignal<ComponentNameSetDto>(emptyComponentNameSet);
  const url = componentNameValue.value.id?.toString() ?? ""
  const { data: componentsSetList, isError, isLoading, refetch, isRefetching } =
    useComponentSet(Number(componentNameValue.value.id), url);

  const defaultProps = {
    options: componentsNameSet ?? {} as ComponentNameSetDto[],
    getOptionLabel: (option: ComponentNameSetDto) => option.name,
  };

  return (
    <Dialog
      maxWidth={"xl"}
      open={dialogOpen.value}
      onClose={() => dialogOpen.value = false}>
      <DialogTitle>Набор компонентов</DialogTitle>
      <DialogContent>
              <SetLayout func={() => {
                updaterComponentNameSet.value(componentNameValue.value);
                dialogOpen.value = false;
              }} />
            <DialogActions sx={{gap: '0.5em'}}>
                <Button color="primary"
                        onClick={() => {
                          updaterComponentNameSet.value(componentNameValue.value);
                          dialogOpen.value = false;
                        }}
                        startIcon={<CheckIcon />}
                        sx={{maxWidth: '200px'}}
                        variant="contained">
                  Выбрать
                </Button>
                <Button color="secondary"
                        onClick={() => {
                          dialogOpen.value = false;
                        }}
                        startIcon={<DoDisturbIcon />}
                        sx={{maxWidth: '200px'}}
                        variant="contained">
                  Закрыть
                </Button>
            </DialogActions>
      </DialogContent>
    </Dialog>
  );
}

export default ComponentsSetDialog;