import { ChevronRight, Settings2 } from "lucide-react";
import {
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from "@/components/ui/sidebar.tsx";
import { SIDEBAR_SETTINGS_ITEMS } from "@/features/appLayout/constants/sidebar.ts";
import { Link } from "react-router-dom";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible.tsx";

export const SidebarSettingsMenu = () => {
  return (
    <Collapsible className="group/collapsible">
      <SidebarMenuItem>
        <CollapsibleTrigger render={<SidebarMenuButton />}>
          <Settings2 />
          <span>Einstellungen</span>
          <ChevronRight className="ml-auto" />
        </CollapsibleTrigger>

        <CollapsibleContent>
          <SidebarMenuSub>
            {SIDEBAR_SETTINGS_ITEMS.map((item) => (
              <SidebarMenuSubItem key={item.href}>
                <SidebarMenuSubButton render={<Link to={item.href} />}>
                  <span>{item.title}</span>
                </SidebarMenuSubButton>
              </SidebarMenuSubItem>
            ))}
          </SidebarMenuSub>
        </CollapsibleContent>
      </SidebarMenuItem>
    </Collapsible>
  );
};
