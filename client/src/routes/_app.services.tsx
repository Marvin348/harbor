import { createFileRoute } from "@tanstack/react-router";
import { ServicesPage } from "@/features/services/pages/ServicesPage.tsx";

export const Route = createFileRoute("/_app/services")({
  component: ServicesPage,
});
