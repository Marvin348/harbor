import { createFileRoute } from "@tanstack/react-router";
import { GeneralSettingsPage } from "@/features/settings/pages/GeneralSettingsPage.tsx";

export const Route = createFileRoute("/_app/settings")({
  component: GeneralSettingsPage,
});
