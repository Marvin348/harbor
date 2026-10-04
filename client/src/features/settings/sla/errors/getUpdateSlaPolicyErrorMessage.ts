import axios from "axios";
import type { ErrorResponse } from "@/api/generated/models/error-response.ts";

export const getUpdateSlaPolicyErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ErrorResponse>(error)) {
    switch (error.response?.status) {
      case 400:
        return "Bitte prüfe die Angaben zur SLA-Richtlinie.";

      case 403:
        return "Du hast keine Berechtigung, SLA-Richtlinien zu bearbeiten.";

      case 404:
        return "Die SLA-Richtlinie wurde nicht gefunden.";

      default:
        return "SLA-Richtlinie konnte nicht aktualisiert werden. Bitte versuche es erneut.";
    }
  }

  return "SLA-Richtlinie konnte nicht aktualisiert werden. Bitte versuche es erneut.";
};
