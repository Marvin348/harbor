import { createFileRoute } from "@tanstack/react-router";
import { SlaPolicySettingsPage } from "@/features/settings/sla/page/SlaPolicySettingsPage.tsx";

export const Route = createFileRoute("/_app/settings/sla-policy")({
  component: SlaPolicySettingsPage,
});
