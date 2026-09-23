import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const TICKET_STATUS_OPTIONS = [
  {
    value: TicketResponseStatusEnum.Open,
    label: "Offen",
  },
  {
    value: TicketResponseStatusEnum.InProgress,
    label: "In Bearbeitung",
  },
  {
    value: TicketResponseStatusEnum.OnHold,
    label: "Wartet",
  },
  {
    value: TicketResponseStatusEnum.Resolved,
    label: "Gelöst",
  },
  {
    value: TicketResponseStatusEnum.Closed,
    label: "Geschlossen",
  },
];

export const TICKET_STATUS_LABELS: Record<TicketResponseStatusEnum, string> = {
  [TicketResponseStatusEnum.Open]: "Offen",
  [TicketResponseStatusEnum.InProgress]: "In Bearbeitung",
  [TicketResponseStatusEnum.OnHold]: "Wartet",
  [TicketResponseStatusEnum.Resolved]: "Gelöst",
  [TicketResponseStatusEnum.Closed]: "Geschlossen",
};
