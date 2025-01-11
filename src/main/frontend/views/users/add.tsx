import React, { useEffect } from 'react';
import {
  Button,
  FormLayout,
  HorizontalLayout,
  Icon, IntegerField, Item,
  ListBox,
  PasswordField,
  Select,
  TextField,
  VerticalLayout
} from '@vaadin/react-components';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useRoles } from 'Frontend/components/api/role';
import { Loading } from 'Frontend/components/config/Loading';
import { useNavigate } from 'react-router-dom';
import { useForm } from '@vaadin/hilla-react-form';
import UserRequestModel from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestModel';
import UserRequest from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequest';
import { usersAddMutation } from 'Frontend/components/api/user';

const responsiveSteps = [
  { minWidth: '0', columns: 1 },
  { minWidth: '500px', columns: 2 },
];

export default function UserAdd() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const {data, isLoading} = useRoles();

  const mutation = usersAddMutation(queryClient, navigate);

  const { model, submit, field, addValidator, value } = useForm(UserRequestModel, {
    onSubmit: async (user) => mutation.mutate(user)
  });

  useEffect(() => {
    if (value.role?.name === "Оператор") {
      value.password = "1111";
      value.repeatPassword = "1111";
    }
  }, [value.role?.name]);

  useEffect(() => {
    addValidator({
      message: 'Пароли должны совпадать',
      validate: (value: UserRequest) => {
        if (value.password != value.repeatPassword) {
          return [{ property: model.password }];
        }
        return [];
      }
    });
  }, []);

  return (
    <>
      { isLoading ? <Loading /> :
        <VerticalLayout theme="spacing padding">
          <FormLayout responsiveSteps={responsiveSteps}>
            <TextField disabled={mutation.isPending} label="Имя пользователя" data-colspan="2" {...field(model.username)} />
            <IntegerField disabled={mutation.isPending} label="Код" stepButtonsVisible min={1} {...field(model.code)} />
            <Select disabled={mutation.isPending} label="Роль" {...field(model.role.name)}>
              <ListBox>
                {data?.map(role => (
                  <Item key={role.name} value={role.name}>
                    {role.name}
                  </Item>
                ))}
              </ListBox>
            </Select>
            <PasswordField disabled={value.role?.name === "Оператор"} label="Пароль" {...field(model.password)} />
            <PasswordField disabled={value.role?.name === "Оператор"} label="Подтвердите пароль" {...field(model.repeatPassword)} />
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
  title: "Добавление пользователя"
};

