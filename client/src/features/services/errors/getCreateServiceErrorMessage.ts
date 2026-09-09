import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const getCreateServiceErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    const statusCode = error.response?.status;

    switch (statusCode) {
      case 400:
        return "Bitte prüfen Sie die Eingaben für den Service.";

      case 403:
        return "Sie haben keine Berechtigung, um einen neuen Service zu erstellen. Bitte wenden Sie sich an Ihren Administrator.";

      case 409:
        return "Ein Service mit diesen Daten oder diesem Namen existiert bereits.";

      default:
        return "Service konnte nicht erstellt werden. Bitte versuchen Sie es erneut.";
    }
  }

  return "Service konnte nicht erstellt werden. Bitte versuchen Sie es erneut.";
};
