import React, { useContext } from 'react';
import TextField from '@mui/material/TextField';
import { Autocomplete, Button, Container, Stack, Typography } from '@mui/material';
import { Loading } from 'Frontend/components/config/Loading';
import ComponentsSetTable from 'Frontend/components/component_set/ComponentsSetTable';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import Box from '@mui/system/Box';
import { emptyComponentNameSet, PropsDialog } from 'Frontend/components/api/helper';
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import CheckIcon from '@mui/icons-material/Check';
import ComponentsBinding from 'Frontend/components/component_binding/ComponentsBinding';
import { Context } from 'Frontend/index';

const ComponentsSet = (props: PropsDialog<ComponentNameSetDto>) => {
  const { func } = props;
  const { data: componentsNameSet, isLoading: isLoadingComponentsNameSet, isError:  isErrorComponentsNameSet,
    isRefetching: isRefetchingComponentsNameSet} = useComponentsNameSet();
  const componentNameInputValue = useContext(Context).componentNameInputValue;
  const componentNameValue = useContext(Context).componentNameValue;

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
          <Stack sx={{marginTop: '.2em'}}>
            <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%'}} >
              <Box sx={{width: '100%', display: 'flex', marginLeft: 'auto'}}>
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
              <Stack sx={{marginTop: '3em'}}>
                <ComponentsSetTable componentNameValue={componentNameValue}/>
                <ComponentsBinding componentNameValue={componentNameValue}/>
              </Stack>
            </Container>
          </Stack>
        </>
      }
    </>
    );
};

export default ComponentsSet;