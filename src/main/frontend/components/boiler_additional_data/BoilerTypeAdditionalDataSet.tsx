import React from 'react';
import TextField from '@mui/material/TextField';
import { Autocomplete, Button, Container, Stack } from '@mui/material';
import { Loading } from 'Frontend/components/config/Loading';
import ComponentsSetTable from 'Frontend/components/component_set/ComponentsSetTable';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import Box from '@mui/system/Box';
import { emptyComponentNameSet, PropsDialog } from 'Frontend/components/api/helper';
import { useSignal } from '@vaadin/hilla-react-signals';
import CheckIcon from '@mui/icons-material/Check';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';
import { useBoilerDataSet } from 'Frontend/components/api/boiler_type_addition_data_set';
import BoilerTypeAdditionalDataValue from 'Frontend/components/boiler_additional_data/BoilerTypeAdditionalDataValue';

const BoilerTypeAdditionalDataSet = (props: PropsDialog<BoilerTypeAdditionalDataSetDto>) => {
  const { func } = props;
  const { data: boilerDataSet, isLoading: isLoadingBoilerDataSet, isError: isErrorBoilerDataSet,
    isRefetching: isRefetchingBoilerDataSet} = useBoilerDataSet();
  const BoilerDataSetInputValue = useSignal<string>("");
  const BoilerDataSetValue = useSignal<BoilerTypeAdditionalDataSetDto>(emptyComponentNameSet);

  const defaultProps = {
    options: boilerDataSet ?? {} as BoilerTypeAdditionalDataSetDto[],
    getOptionLabel: (option: BoilerTypeAdditionalDataSetDto) => option.name,
  };

  if (isErrorBoilerDataSet) {
    return (<div style={{color: 'red'}}>Не удалось загрузить набор данных</div>)
  }

  return (
    <>
      {isRefetchingBoilerDataSet || isLoadingBoilerDataSet ? <Loading /> :
        <>
          <Stack sx={{marginTop: '2em'}}>
            <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%'}} >
              <Box sx={{width: '100%', display: 'flex', marginTop: '1em', marginLeft: 'auto'}}>
                <Box sx={{display: 'flex', marginLeft: 'auto', gap: '1em'}}>
                  {func !== undefined &&
                    <Button color="primary"
                            onClick={() => {
                              func(BoilerDataSetValue.value);
                            }}
                            startIcon={<CheckIcon />}
                            sx={{maxWidth: '200px'}}
                            variant="contained">
                      Выбрать
                    </Button>
                  }
                  <Autocomplete
                    {...defaultProps}
                    value={BoilerDataSetValue.value}
                    onChange={(_, newValue: ComponentNameSetDto | null) => {
                      BoilerDataSetValue.value = newValue ?? emptyComponentNameSet;
                    }}
                    inputValue={BoilerDataSetInputValue.value}
                    onInputChange={(_, newInputValue: string | null) => {
                      BoilerDataSetInputValue.value = newInputValue ?? "";
                    }}
                    loading={isLoadingBoilerDataSet || isRefetchingBoilerDataSet}
                    disabled={isLoadingBoilerDataSet || isRefetchingBoilerDataSet}
                    size="small"
                    noOptionsText={"Не найдено"}
                    renderInput={(params) => <TextField label={"Выберите набор"} {...params} />}
                    sx={{ backgroundColor: '#1A39601A', minWidth: '300px' }}
                  />
                </Box>
              </Box>
              <BoilerTypeAdditionalDataValue boilerDataSetValue={BoilerDataSetValue}/>
            </Container>
          </Stack>
        </>
      }
    </>
    );
};

export default BoilerTypeAdditionalDataSet;