import type { ReactNode } from "react";
import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";
import { Spinner } from "@/components/ui/spinner.tsx";
import { Navigate, Outlet } from "@tanstack/react-router";

type ProtectedRouteProps = {
  children?: ReactNode;
};

export const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const { user, isLoading, error } = useCurrentUser();

  if (isLoading) return <Spinner />;

  if (!user || error) {
    return <Navigate to="/login" replace />;
  }

  return children ?? <Outlet />;
};
