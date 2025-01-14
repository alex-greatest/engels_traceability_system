import { configureAuth } from '@vaadin/hilla-react-auth';
import { UserInfoController } from 'Frontend/generated/endpoints';

const auth = configureAuth(UserInfoController.getUserInfo, {
    getRoles: (userInfo) => userInfo.authorities?.map((v) => v ?? '') ?? [],
});

// Export auth provider and useAuth hook, which are automatically
// typed to the result of `UserInfoService.getUserInfo`
export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;
