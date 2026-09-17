import {
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar.tsx";
import { Link } from "@tanstack/react-router";

type SidebarNavItemProps = {
  label: string;
  href: string;
  icon: React.ElementType;
};

export const SidebarNavItem = ({
  icon: Icon,
  label,
  href,
}: SidebarNavItemProps) => {
  return (
    <SidebarMenuItem>
      <SidebarMenuButton render={<Link to={href} />}>
        <Icon />
        <span>{label}</span>
      </SidebarMenuButton>
    </SidebarMenuItem>
  );
};
