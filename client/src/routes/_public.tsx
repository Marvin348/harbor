import { createFileRoute } from "@tanstack/react-router";
import { PublicLayout } from "@/features/auth/pages/PublicLayout.tsx";

export const Route = createFileRoute("/_public")({
  component: PublicLayout,
});
