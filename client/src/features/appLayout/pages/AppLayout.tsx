import { Outlet } from "@tanstack/react-router";
import { AppSidebar } from "@/features/appLayout/components/sidebar/AppSidebar.tsx";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar.tsx";
import { AppHeader } from "@/features/appLayout/components/AppHeader.tsx";

export const AppLayout = () => {
  return (
    <SidebarProvider>
      <AppSidebar />

      <SidebarInset>
        <AppHeader />

        <main className="p-6">
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider>
  );
};
