import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
} from "@/components/ui/sidebar.tsx";
import { SIDEBAR_ITEMS } from "@/features/appLayout/constants/sidebar.ts";
import { SidebarNavItem } from "@/features/appLayout/components/sidebar/SidebarNavItem.tsx";
import { SidebarSettingsMenu } from "@/features/appLayout/components/sidebar/SidebarSettingsMenu.tsx";
import { NavUser } from "@/features/appLayout/components/sidebar/NavUser.tsx";
import { OrganizationInfo } from "@/features/appLayout/components/sidebar/OrganizationInfo.tsx";

export const AppSidebar = () => {
  return (
    <Sidebar collapsible="icon">
      <SidebarHeader>
        <OrganizationInfo />
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Navigation</SidebarGroupLabel>

          <SidebarGroupContent>
            <SidebarMenu>
              {SIDEBAR_ITEMS.map((item) => (
                <SidebarNavItem
                  key={item.href}
                  icon={item.icon}
                  label={item.title}
                  href={item.href}
                />
              ))}

              <SidebarSettingsMenu />
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <NavUser />
      </SidebarFooter>
    </Sidebar>
  );
};
