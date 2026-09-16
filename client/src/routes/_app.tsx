import { createFileRoute } from "@tanstack/react-router";
import { AppLayout } from "@/features/appLayout/pages/AppLayout.tsx";
import { ProtectedRoute } from "@/features/auth/pages/ProtectedRoute.tsx";

const AppRoute = () => {
  return (
    <ProtectedRoute>
      <AppLayout />
    </ProtectedRoute>
  );
};

export const Route = createFileRoute("/_app")({
  component: AppRoute,
});
