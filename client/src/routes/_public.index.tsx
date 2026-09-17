import { createFileRoute, Navigate } from "@tanstack/react-router";

export const Route = createFileRoute("/_public/")({
  component: () => <Navigate to="/login" replace />,
});
