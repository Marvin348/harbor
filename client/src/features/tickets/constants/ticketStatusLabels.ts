import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const TICKET_STATUS_LABELS: Record<TicketResponseStatusEnum, string> = {
  [TicketResponseStatusEnum.Open]: "Offen",
  [TicketResponseStatusEnum.InProgress]: "In Bearbeitung",
  [TicketResponseStatusEnum.OnHold]: "Wartet",
  [TicketResponseStatusEnum.Resolved]: "Gelöst",
  [TicketResponseStatusEnum.Closed]: "Geschlossen",
};
