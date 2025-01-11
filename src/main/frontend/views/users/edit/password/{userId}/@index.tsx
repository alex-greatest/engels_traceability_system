import React, { useEffect } from 'react';
import {
    Button,
    FormLayout,
    HorizontalLayout,
    Icon,
    PasswordField,
    VerticalLayout
} from '@vaadin/react-components';
import { useQueryClient } from '@tanstack/react-query';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from '@vaadin/hilla-react-form';
import UserRequest from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequest';
import { usersEditPasswordMutation } from 'Frontend/components/api/user';
import UserRequestPasswordModel from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestPasswordModel';

const responsiveSteps = [
    { minWidth: '0', columns: 1 },
    { minWidth: '500px', columns: 2 },
];

export default function UserEditPassword() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const { userId } = useParams();

    const mutation = usersEditPasswordMutation(queryClient, navigate);

    const { model, submit, field, addValidator } = useForm(UserRequestPasswordModel, {
        onSubmit: async (user) => mutation.mutate({code: Number(userId), user: user})
    });

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
          <VerticalLayout theme="spacing padding">
              <FormLayout responsiveSteps={responsiveSteps}>
                  <PasswordField disabled={mutation.isPending} label="Пароль" {...field(model.password)} />
                  <PasswordField disabled={mutation.isPending} label="Подтвердите пароль" {...field(model.repeatPassword)} />
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
      </>
    )
}

export const config: ViewConfig = {
    loginRequired: true,
    rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
    title: "Изменение пароля пользователя"
};

