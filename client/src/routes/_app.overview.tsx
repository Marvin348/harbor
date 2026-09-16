import { createFileRoute } from "@tanstack/react-router";
import { OverviewPage } from "@/features/overview/pages/OverviewPage.tsx";

export const Route = createFileRoute("/_app/overview")({
  component: OverviewPage,
});
