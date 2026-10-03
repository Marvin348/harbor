import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const getCreateSlaPolicyErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    switch (error.response?.status) {
      case 400:
        return "Bitte prüfe die Angaben zur SLA-Richtlinie.";

      case 403:
        return "Du hast keine Berechtigung, SLA-Richtlinien zu erstellen.";

      case 409:
        return "Für diese Priorität existiert bereits eine SLA-Richtlinie.";

      default:
        return "SLA-Richtlinie konnte nicht erstellt werden. Bitte versuche es erneut.";
    }
  }

  return "SLA-Richtlinie konnte nicht erstellt werden. Bitte versuche es erneut.";
};
