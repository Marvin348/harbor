import { Outlet } from "@tanstack/react-router";
import { SettingsNavigation } from "@/features/settings/components/SettingsNavigation.tsx";
import { SettingsHeader } from "@/features/settings/components/SettingsHeader.tsx";

export const GeneralSettingsPage = () => {
  return (
    <div className="mx-auto w-full max-w-screen-2xl">
      <SettingsHeader />

      <div className="flex flex-col sm:flex-row gap-6 lg:gap-8">
        <SettingsNavigation />

        <section className="min-w-0 ">
          <Outlet />
        </section>
      </div>
    </div>
  );
};
