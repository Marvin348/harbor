import { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";

export const TICKET_PRIORITY_OPTIONS = [
  {
    value: TicketResponsePriorityEnum.Low,
    label: "Niedrig",
  },
  {
    value: TicketResponsePriorityEnum.Medium,
    label: "Normal",
  },
  {
    value: TicketResponsePriorityEnum.High,
    label: "Hoch",
  },
  {
    value: TicketResponsePriorityEnum.Critical,
    label: "Kritisch",
  },
];

export const TICKET_PRIORITY_LABELS: Record<
  TicketResponsePriorityEnum,
  string
> = {
  [TicketResponsePriorityEnum.Low]: "Niedrig",
  [TicketResponsePriorityEnum.Medium]: "Normal",
  [TicketResponsePriorityEnum.High]: "Hoch",
  [TicketResponsePriorityEnum.Critical]: "Kritisch",
};
