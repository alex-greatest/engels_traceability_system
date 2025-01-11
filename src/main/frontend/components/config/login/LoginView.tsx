import { LoginI18n, LoginOverlay } from '@vaadin/react-components';
import {useSignal} from "@vaadin/hilla-react-signals";
import {useAuth} from "Frontend/components/config/auth/auth";

const loginI18nDefault: LoginI18n = {
  form: {
    title: '',
    username: 'Имя пользователя',
    password: 'Пароль',
    submit: 'Войти',
    forgotPassword: 'Forgot password',
  },
  header: { title: 'Вход', description: '' },
  errorMessage: {
    title: 'Неккоректное имя или пароль',
    message: 'Проверьте логин или пароль и повторите ввод.',
    username: 'Имя пользователя обязательно для заполнения',
    password: 'Пароль обязателен для заполнения',
  },
};

export default function LoginView() {
    const { login } = useAuth();
    const hasError = useSignal(false);
    const hasDisable = useSignal(false);

    if (hasError.value && hasDisable.value)
    {
        hasDisable.value = false;
    }

    return (
        <LoginOverlay
            opened
            error={hasError.value}
            noForgotPassword
            onLogin={async ({ detail: { username, password } }) => {
                const { error } = await login(username, password);
                hasError.value = Boolean(error);
            }}
            onErrorChanged={() => hasDisable.value = true}
            disabled={hasDisable.value}
            i18n={loginI18nDefault}
        />
    );
}