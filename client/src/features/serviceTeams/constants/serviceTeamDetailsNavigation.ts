export const SERVICE_TEAM_DETAILS_NAVIGATION_ITEMS = [
  {
    label: "Übersicht",
    to: "/service-teams/$id" as const,
  },
  {
    label: "Tickets",
    to: "/service-teams/$id/tickets" as const,
  },
  {
    label: "Services",
    to: "/service-teams/$id/services" as const,
  },
  {
    label: "Agents",
    to: "/service-teams/$id/agents" as const,
  },
];
