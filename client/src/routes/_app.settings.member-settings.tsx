import { createFileRoute } from "@tanstack/react-router";
import { MembersSettingsPage } from "@/features/settings/pages/MemberSettingsPage.tsx";

export const Route = createFileRoute("/_app/settings/member-settings")({
  component: MembersSettingsPage,
});
