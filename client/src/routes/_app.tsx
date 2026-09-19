import { createFileRoute } from "@tanstack/react-router";
import { AppLayout } from "@/features/appLayout/pages/AppLayout.tsx";
import { ProtectedRoute } from "@/features/auth/pages/ProtectedRoute.tsx";

export const Route = createFileRoute("/_app")({
  component: () => (
    <ProtectedRoute>
      <AppLayout />
    </ProtectedRoute>
  ),
});
