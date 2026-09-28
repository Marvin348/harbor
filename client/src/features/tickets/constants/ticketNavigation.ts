export const TICKET_NAVIGATION = [
  { label: "Kommunikation", to: "/tickets/$id" },
  {
    label: "Ticket",
    to: "/tickets/$id/details",
  },
  {
    label: "Aktivität",
    to: "/tickets/$id/activity",
  },
  {
    label: "Notizen",
    to: "/tickets/$id/notes",
  },
] as const;
