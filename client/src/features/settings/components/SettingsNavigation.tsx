import { Link } from "@tanstack/react-router";
import { SETTINGS_NAVIGATION_ITEMS } from "@/features/settings/constants/settingsNavigation.ts";

export const SettingsNavigation = () => {
  return (
    <aside className="min-w-50">
      <nav aria-label="Einstellungsbereiche" className="flex flex-col gap-1">
        {SETTINGS_NAVIGATION_ITEMS.map((item) => (
          <Link
            key={item.to}
            to={item.to}
            activeOptions={{ exact: true }}
            className="flex items-center gap-3 rounded-md px-3 py-2 text-sm  text-foreground transition-colors hover:text-foreground"
            activeProps={{
              className: "bg-muted font-medium",
            }}
          >
            <span>{item.title}</span>
          </Link>
        ))}
      </nav>
    </aside>
  );
};
