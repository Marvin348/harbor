import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const getCreateTicketErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    const statusCode = error.response?.status;

    if (statusCode === 404) {
      return "Der ausgewählte Service konnte nicht gefunden werden.";
    }
  }

  return "Anfrage konnte nicht erstellt werden. Bitte versuchen Sie es erneut.";
};
