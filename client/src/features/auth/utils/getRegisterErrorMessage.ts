import axios from "axios";

export const getRegisterErrorMessage = (error: unknown) => {
  if (!axios.isAxiosError(error)) {
    return "Registrierung fehlgeschlagen.";
  }

  if (error.response?.status === 409) {
    return "Diese E-Mail-Adresse wird bereits verwendet.";
  }

  return "Registrierung fehlgeschlagen. Bitte versuchen Sie es erneut.";
};
