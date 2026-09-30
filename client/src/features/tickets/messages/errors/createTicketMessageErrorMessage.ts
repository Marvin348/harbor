import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const createTicketMessageErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    const statusCode = error.response?.status;

    switch (statusCode) {
      case 400:
        return "Die Nachricht ist ungültig. Bitte überprüfe deine Eingabe.";

      case 401:
        return "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an.";

      case 403:
        return "Als Requester kannst du keine interne Notiz erstellen.";

      case 404:
        return "Das Ticket wurde nicht gefunden oder du hast keinen Zugriff darauf.";

      default:
        return "Die Nachricht konnte nicht gesendet werden. Bitte versuche es erneut.";
    }
  }

  return "Die Nachricht konnte nicht gesendet werden. Bitte versuche es erneut.";
};
