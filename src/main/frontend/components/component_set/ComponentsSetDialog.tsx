import { Signal, useSignal } from '@vaadin/hilla-react-signals';
import { useComponentSet } from 'Frontend/components/api/components_set';
import ComponentsSetTable from 'Frontend/components/component_set/ComponentsSetTable';
import React from 'react';
import {
  Autocomplete,
  Button,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack
} from '@mui/material';
import TextField from '@mui/material/TextField';
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import Box from '@mui/system/Box';
import { emptyComponentNameSet } from 'Frontend/components/api/helper';
import CheckIcon from '@mui/icons-material/Check';
import { Loading } from 'Frontend/components/config/Loading';

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
        { isLoadingComponentsNameSet ? <Loading /> :
          <>
            <Stack>
              <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%'}} >
                <Box sx={{width: '100%', display: 'flex', marginTop: '1em', marginLeft: 'auto'}}>
                  <Box sx={{display: 'flex', marginLeft: 'auto', gap: '1em'}}>
                    <Autocomplete
                      {...defaultProps}
                      value={componentNameValue.value}
                      onChange={(_, newValue: ComponentNameSetDto | null) => {
                        componentNameValue.value = newValue ?? emptyComponentNameSet;
                      }}
                      inputValue={componentNameInputValue.value}
                      onInputChange={(_, newInputValue: string | null) => {
                        componentNameInputValue.value = newInputValue ?? "";
                      }}
                      loading={isLoadingComponentsNameSet}
                      disabled={isLoadingComponentsNameSet}
                      size="small"
                      noOptionsText={"Не найдено"}
                      renderInput={(params) => <TextField label={"Выберите набор"} {...params} />}
                      sx={{ backgroundColor: '#1A39601A', minWidth: '300px' }}
                    />
                  </Box>
                </Box>
                <ComponentsSetTable componentNameSetId={componentNameValue.value.id?.toString()}
                                    data={componentsSetList} isError={isError || isErrorComponentsNameSet}
                                    isLoading={isLoading || isLoadingComponentsNameSet} refetch={refetch}
                                    isRefetching={isRefetching || isRefetchingComponentsNameSet}
                                    url={url} maxHeight={"400px"} minHeight={"400px"}/>
              </Container>
            </Stack>
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
          </>
        }
      </DialogContent>
    </Dialog>
  );
}

export default ComponentsSetDialog;