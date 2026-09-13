import { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";

export const TICKET_PRIORITY_LABELS: Record<
  TicketResponsePriorityEnum,
  string
> = {
  [TicketResponsePriorityEnum.Low]: "Niedrig",
  [TicketResponsePriorityEnum.Medium]: "Normal",
  [TicketResponsePriorityEnum.High]: "Hoch",
  [TicketResponsePriorityEnum.Critical]: "Kritisch",
};
