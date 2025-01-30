import { MRT_Row } from 'material-react-table';
import BoilerTypeDto from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeDto';

export const validateRequired = (value: string) => !!value.trim().length;

export const errorMessageLength50 = "Длина должна быть больше 0 и меньше 50";

export const errorMessageEmpty = "Поле не может быть пустым";

export const validateLength = (value: string) => value.length > 0 && value.length <= 50;

export const emptyComponentNameSet = {id: undefined, name: ""};

export const emptyBoilerType = {name: "", article: "", componentNameSet: {name: ""}};

export interface PropsLambdaVoid {
  func?: () => void;
}