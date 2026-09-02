import { createBrowserRouter } from "react-router";
import { AppLayout } from "@/AppLayout";
import { RegisterPage } from "@/features/auth/pages/RegisterPage.tsx";
import { LoginPage } from "@/features/auth/pages/LoginPage.tsx";
import { ProtectedRoute } from "@/features/auth/pages/ProtectedRoute.tsx";
import { PublicLayout } from "@/features/auth/pages/PublicLayout.tsx";
import { OverviewPage } from "@/features/auth/pages/OverviewPage.tsx";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      { index: true, path: "login", element: <LoginPage /> },
      { path: "register", element: <RegisterPage /> },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <AppLayout />,
        children: [{ path: "overview", element: <OverviewPage /> }],
      },
    ],
  },
]);
