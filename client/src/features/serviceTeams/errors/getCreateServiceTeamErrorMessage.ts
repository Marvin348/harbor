import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const getCreateServiceTeamErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    const statusCode = error.response?.status;

    switch (statusCode) {
      case 400:
        return "Bitte prüfen Sie die Eingaben für das Service-Team.";

      case 403:
        return "Sie haben keine Berechtigung, um ein neues Service-Team zu erstellen. Bitte wenden Sie sich an Ihren Administrator.";

      case 404:
        return "Die zugehörige Organisation konnte nicht gefunden werden.";

      case 409:
        return "Ein Service-Team mit diesem Namen existiert bereits.";

      default:
        return "Service-Team konnte nicht erstellt werden. Bitte versuchen Sie es erneut.";
    }
  }

  return "Service-Team konnte nicht erstellt werden. Bitte versuchen Sie es erneut.";
};
