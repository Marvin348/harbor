import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const claimTicketErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    const statusCode = error.response?.status;

    switch (statusCode) {
      case 400:
        return "Das Ticket konnte aufgrund einer ungültigen Anfrage nicht übernommen werden.";

      case 401:
        return "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an.";

      case 403:
        return "Du hast keine Berechtigung, dieses Ticket zu übernehmen.";

      case 404:
        return "Das Ticket wurde nicht gefunden oder du hast keinen Zugriff darauf.";

      case 409:
        return "Das Ticket wurde bereits übernommen und kann nicht erneut übernommen werden.";

      default:
        return "Das Ticket konnte nicht übernommen werden. Bitte versuche es erneut.";
    }
  }

  return "Das Ticket konnte nicht übernommen werden. Bitte versuche es erneut.";
};
