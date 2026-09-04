import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar.tsx";
import { ChevronsUpDown, LogOut, Settings2, User } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx";
import { Link } from "react-router-dom";

export const NavUser = () => {
  const { user } = useCurrentUser();
  const { isMobile } = useSidebar();

  if (!user) {
    return null;
  }

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger render={<SidebarMenuButton size="lg" />}>
            <div className="size-8 shrink-0 rounded-full bg-primary" />

            <div className="grid flex-1 text-left text-sm leading-tight">
              <span className="truncate font-medium">{`${user.firstName} ${user.lastName}`}</span>

              <span className="truncate text-xs text-muted-foreground">
                {user.email}
              </span>
            </div>

            <ChevronsUpDown className="ml-auto size-4" />
          </DropdownMenuTrigger>

          <DropdownMenuContent
            side={isMobile ? "bottom" : "right"}
            align="end"
            className="min-w-56"
          >
            <DropdownMenuItem render={<Link to="/settings/member-settings" />}>
              <User />
              Account
            </DropdownMenuItem>

            <DropdownMenuItem render={<Link to="/settings" />}>
              <Settings2 />
              Einstellungen
            </DropdownMenuItem>

            <DropdownMenuSeparator />

            <DropdownMenuItem>
              <LogOut />
              Log out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  );
};
