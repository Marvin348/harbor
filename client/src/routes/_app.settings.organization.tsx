import { createFileRoute } from "@tanstack/react-router";
import { OrganizationSettingsPage } from "@/features/settings/pages/OrganizationSettingsPage.tsx";

export const Route = createFileRoute("/_app/settings/organization")({
  component: OrganizationSettingsPage,
});
