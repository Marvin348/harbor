import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";
import { Spinner } from "@/components/ui/spinner.tsx";
import { Navigate, Outlet } from "react-router-dom";

export const ProtectedRoute = () => {
  const { user, isLoading, error } = useCurrentUser();

  if (isLoading) return <Spinner />;

  if (!user || error) {
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};
