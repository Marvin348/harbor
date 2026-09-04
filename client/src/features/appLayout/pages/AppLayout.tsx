import { Outlet } from "react-router-dom";
import { AppSidebar } from "@/features/appLayout/components/sidebar/AppSidebar.tsx";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar.tsx";
import { AppHeader } from "@/features/appLayout/components/AppHeader.tsx";

export const AppLayout = () => {
  return (
    <SidebarProvider>
      <AppSidebar />

      <SidebarInset>
        <AppHeader />

        <main>
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider>
  );
};
