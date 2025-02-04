
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';

export const validateRequired = (value: string) => !!value.trim().length;

export const errorMessageLength30 = "Длина должна быть больше 0 и меньше 30";

export const errorMessageLength50 = "Длина должна быть больше 0 и меньше 50";

export const errorMessageLength100 = "Длина должна быть больше 0 и меньше 100";

export const errorMessageEmpty = "Поле не может быть пустым";

export const validateLength = (value: string) => value.length > 0 && value.length <= 50;

export const validateLength100 = (value: string) => value.length > 0 && value.length <= 100;

export const validateLength30 = (value: string) => value.length > 0 && value.length <= 30;

export const emptyComponentNameSet = {id: undefined, name: ""};

export const emptyBoilerType = {name: "", article: "", componentNameSet: {name: ""}};

export interface PropsDialog<T> {
  func?: (componentNameSet: T) => void;
}

export interface PropsBoilerTypeDataSet {
  func?: (boilerTypeAdditionalDataSetDto: BoilerTypeAdditionalDataSetDto) => void;
}