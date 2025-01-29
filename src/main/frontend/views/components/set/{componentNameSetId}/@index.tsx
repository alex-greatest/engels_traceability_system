import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { VerticalLayout } from '@vaadin/react-components/VerticalLayout.js';
import React from 'react';
import { FormLayout } from '@vaadin/react-components';
import TextField from '@mui/material/TextField';
import {  Button, Container } from '@mui/material'
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import {
  useComponentSet,
} from 'Frontend/components/api/components_set';
import { Loading } from 'Frontend/components/config/Loading';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ComponentsSetTable from 'Frontend/components/component_set/ComponentsSetTable';

const responsiveSteps = [
  { minWidth: '0', columns: 2 },
  { minWidth: '500px', columns: 2 }
];

const ComponentsSet = () => {
  const url = useLocation().key;
  const navigate = useNavigate();
  const { componentNameSetId } = useParams();
  const { data: componentsSetList, isError, isLoading, refetch, isRefetching } =
    useComponentSet(Number(componentNameSetId), url);

  return (
    <>
      {isLoading ? <Loading /> :
        <>
          <VerticalLayout theme="spacing padding">
            <Container maxWidth={'xl'} sx={{
              display: 'flex',
              flexDirection: 'column',
              width: '100%',
              height: '100%',
              marginTop: '2em'
            }}>
              <FormLayout responsiveSteps={responsiveSteps}>
                <TextField
                  data-colspan="1"
                  label="Название набора"
                  id="outlined-size-small"
                  value={componentsSetList?.componentNameSet.name}
                  size="small"
                  slotProps={{
                    input: {
                      readOnly: true
                    }
                  }}
                  sx={{ backgroundColor: '#1A39601A' }}
                />
              </FormLayout>
              <ComponentsSetTable componentNameSetId={componentNameSetId}
                                  data={componentsSetList} isError={isError}
                                  isLoading={isLoading} refetch={refetch} isRefetching={isRefetching}
                                  url={url} minHeight={"0px"} maxHeight={"1000px"}/>
              <Button color="secondary"
                      onClick={() => navigate('/components/name_set')}
                      startIcon={<ArrowBackIcon />}
                      sx={{maxWidth: '250px', marginTop: '3em'}}
                      variant="contained">
                Вернуться к списку
              </Button>
            </Container>
          </VerticalLayout>
        </>
      }
    </>
    );
};

export default ComponentsSet;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Создание набора компонентов"
};
