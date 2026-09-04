import { createBrowserRouter } from "react-router";
import { AppLayout } from "@/features/appLayout/pages/AppLayout.tsx";
import { RegisterPage } from "@/features/auth/pages/RegisterPage.tsx";
import { LoginPage } from "@/features/auth/pages/LoginPage.tsx";
import { ProtectedRoute } from "@/features/auth/pages/ProtectedRoute.tsx";
import { PublicLayout } from "@/features/auth/pages/PublicLayout.tsx";
import { OverviewPage } from "@/features/overview/pages/OverviewPage.tsx";
import { Navigate } from "react-router-dom";
import { TicketsPage } from "@/features/tickets/pages/TicketsPage.tsx";
import { ServicesPage } from "@/features/services/pages/ServicesPage.tsx";
import { GeneralSettingsPage } from "@/features/settings/pages/GeneralSettingsPage.tsx";
import { OrganizationSettingsPage } from "@/features/settings/pages/OrganizationSettingsPage.tsx";
import { ServiceTeamsPage } from "@/features/serviceTeams/pages/ServiceTeamsPage.tsx";
import { MembersSettingsPage } from "@/features/settings/pages/MemberSettingsPage.tsx";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      { index: true, element: <Navigate to="/login" replace /> },

      { path: "login", element: <LoginPage /> },
      { path: "register", element: <RegisterPage /> },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <AppLayout />,
        children: [
          { path: "overview", element: <OverviewPage /> },
          { path: "tickets", element: <TicketsPage /> },
          { path: "services", element: <ServicesPage /> },
          { path: "service-teams", element: <ServiceTeamsPage /> },

          {
            path: "settings",
            element: <GeneralSettingsPage />,
            children: [
              { path: "organization", element: <OrganizationSettingsPage /> },
              { path: "member-settings", element: <MembersSettingsPage /> },
            ],
          },
        ],
      },
    ],
  },
]);
