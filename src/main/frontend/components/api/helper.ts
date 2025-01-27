export const validateRequired = (value: string) => !!value.trim().length;

export const errorMessageLength50 = "Длина должна быть больше 0 и меньше 50";

export const errorMessageEmpty = "Поле не может быть пустым";

export const validateLength = (value: string) => value.length > 0 && value.length <= 50 ;