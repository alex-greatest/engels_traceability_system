import { toast } from 'react-toastify';

export const showSuccessMessage = (toastId:string, message: string, duration: number = 5000) => {
  toast.success(message, {
    toastId: toastId,
    position: "top-right",
    autoClose: duration,
    closeOnClick: true,
  });
};

export const showErrorMessage = (toastId:string, message: string, duration: number = 5000) => {
  toast.error(message, {
    toastId: toastId,
    position: "top-right",
    autoClose: duration,
    closeOnClick: true,
  });
};