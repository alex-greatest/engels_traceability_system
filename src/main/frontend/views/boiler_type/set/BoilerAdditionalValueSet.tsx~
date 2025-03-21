import React from 'react';
import TextField from '@mui/material/TextField';
import { Autocomplete, Button, Container, Stack } from '@mui/material';
import { Loading } from 'Frontend/components/config/Loading';
import ComponentsSetTable from 'Frontend/components/component_set/ComponentsSetTable';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import Box from '@mui/system/Box';
import { emptyComponentNameSet, PropsComponentSet } from 'Frontend/components/api/helper';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import CheckIcon from '@mui/icons-material/Check';

const ComponentsSet = (props: PropsComponentSet) => {
  const { func } = props;
  const { data: componentsNameSet, isLoading: isLoadingComponentsNameSet, isError:  isErrorComponentsNameSet,
    isRefetching: isRefetchingComponentsNameSet} = useComponentsNameSet();
  const componentNameInputValue = useSignal<string>("");
  const componentNameValue = useSignal<ComponentNameSetDto>(emptyComponentNameSet);

  const defaultProps = {
    options: componentsNameSet ?? {} as ComponentNameSetDto[],
    getOptionLabel: (option: ComponentNameSetDto) => option.name,
  };

  if (isErrorComponentsNameSet) {
    return (<div style={{color: 'red'}}>Не удалось загрузить набор компонентов</div>)
  }

  return (
    <>
      {isRefetchingComponentsNameSet || isLoadingComponentsNameSet ? <Loading /> :
        <>
          <Stack sx={{marginTop: '2em'}}>
            <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%'}} >
              <Box sx={{width: '100%', display: 'flex', marginTop: '1em', marginLeft: 'auto'}}>
                <Box sx={{display: 'flex', marginLeft: 'auto', gap: '1em'}}>
                  {func !== undefined &&
                    <Button color="primary"
                            onClick={() => {
                              func(componentNameValue.value);
                            }}
                            startIcon={<CheckIcon />}
                            sx={{maxWidth: '200px'}}
                            variant="contained">
                      Выбрать
                    </Button>
                  }
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
                    loading={isLoadingComponentsNameSet || isRefetchingComponentsNameSet}
                    disabled={isLoadingComponentsNameSet || isRefetchingComponentsNameSet}
                    size="small"
                    noOptionsText={"Не найдено"}
                    renderInput={(params) => <TextField label={"Выберите набор"} {...params} />}
                    sx={{ backgroundColor: '#1A39601A', minWidth: '300px' }}
                  />
                </Box>
              </Box>
              <ComponentsSetTable componentNameValue={componentNameValue}/>
            </Container>
          </Stack>
        </>
      }
    </>
    );
};

export default ComponentsSet;