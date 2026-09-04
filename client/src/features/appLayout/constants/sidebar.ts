import {
  House,
  Ticket,
  Boxes,
  Users,
  Building2,
  Settings2,
} from "lucide-react";

export const SIDEBAR_ITEMS = [
  {
    title: "Übersicht",
    href: "/overview",
    icon: House,
  },
  {
    title: "Tickets",
    href: "/tickets",
    icon: Ticket,
  },
  {
    title: "Services",
    href: "/services",
    icon: Boxes,
  },
  {
    title: "Service-Teams",
    href: "/service-teams",
    icon: Users,
  },
];

export const SIDEBAR_SETTINGS_ITEMS = [
  {
    title: "Organisation",
    href: "/settings/organization",
    icon: Building2,
  },
  {
    title: "Einstellungen",
    href: "/settings",
    icon: Settings2,
  },
];
