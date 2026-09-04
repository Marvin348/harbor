import { SidebarTrigger } from "@/components/ui/sidebar.tsx";

export const AppHeader = () => {
  return (
    <header className="flex h-14 items-center border-b px-4">
      <SidebarTrigger />

      <div className="ml-auto">HEADER{/* User / Notifications / etc. */}</div>
    </header>
  );
};
