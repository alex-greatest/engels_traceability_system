import React, { useEffect } from 'react';
import {
    Button,
    FormLayout,
    HorizontalLayout,
    Icon, IntegerField, Item,
    ListBox,
    Select,
    TextField,
    VerticalLayout
} from '@vaadin/react-components';
import { useQueryClient } from '@tanstack/react-query';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useRoles } from 'Frontend/components/api/role';
import { Loading } from 'Frontend/components/config/Loading';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from '@vaadin/hilla-react-form';
import UserRequestModel from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestModel';
import { EndpointError } from '@vaadin/hilla-frontend';
import { showErrorMessage } from 'Frontend/components/config/notification';
import { usersEditMutation, useUser } from 'Frontend/components/api/user';

const responsiveSteps = [
    { minWidth: '0', columns: 1 },
    { minWidth: '500px', columns: 2 },
];

export default function UserEdit() {
    const queryClient = useQueryClient();
    const { userId } = useParams();
    const navigate = useNavigate();
    const { data: roles, isLoading: isLoadingRole } = useRoles();
    const { data: user, isLoading: isLoadingUser, isSuccess: isSuccessUser, error: errorUser, isError: isErrorUser,
        isRefetching: isRefetchingUser } = useUser(Number(userId));

    const mutation = usersEditMutation(queryClient, navigate);

    const { model, submit, field, read } = useForm(UserRequestModel, {
        onSubmit: async (user) => mutation.mutate({code: Number(userId), user: user})
    });

    useEffect(() => {
        if (isSuccessUser && !isRefetchingUser) {
            read(user);
        }
    }, [isSuccessUser, isRefetchingUser]);

    if (isErrorUser) {
        if (errorUser instanceof EndpointError && errorUser.type?.includes("RecordNotFoundException")) {
            showErrorMessage("users_edit_error", errorUser.message);
        } else {
            showErrorMessage("users_edit_error", "Неизвестная ошибка");
        }
    }

    return (
      <>
          { isLoadingRole || isLoadingUser || isRefetchingUser ? <Loading /> :
            <VerticalLayout theme="spacing padding">
                <FormLayout responsiveSteps={responsiveSteps}>
                    <TextField disabled={mutation.isPending} label="Имя пользователя" data-colspan="2" {...field(model.username)} />
                    <IntegerField disabled={mutation.isPending} label="Код" stepButtonsVisible min={1} {...field(model.code)} />
                    <Select disabled={mutation.isPending} label="Роль" {...field(model.role.name)}>
                        <ListBox>
                            {roles?.map(role => (
                              <Item key={role.name} value={role.name}>
                                  {role.name}
                              </Item>
                            ))}
                        </ListBox>
                    </Select>
                </FormLayout>
                <HorizontalLayout theme="spacing">
                    <Button disabled={mutation.isPending} onClick={submit} theme="primary">
                        <Icon icon="vaadin:check" />
                        Подтвердить
                    </Button>
                    <Button disabled={mutation.isPending} onClick={() => navigate("/users")} theme="secondary">
                        <Icon icon="vaadin:close" />
                        Отмена
                    </Button>
                </HorizontalLayout>
            </VerticalLayout>
          }
      </>
    )
}

export const config: ViewConfig = {
    loginRequired: true,
    rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
    title: "Редактирование данных пользователя"
};

