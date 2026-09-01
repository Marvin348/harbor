import { createBrowserRouter } from "react-router";
import { AppLayout } from "@/AppLayout";
import { RegisterPage } from "@/features/auth/pages/RegisterPage.tsx";
import { LoginPage } from "@/features/auth/pages/LoginPage.tsx";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { path: "register", element: <RegisterPage /> },
      { path: "login", element: <LoginPage /> },
    ],
  },
]);
