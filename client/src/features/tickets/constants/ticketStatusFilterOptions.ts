import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";
import { TICKET_STATUS_LABELS } from "@/features/tickets/constants/ticketStatusLabels.ts";

type TicketStatusFilterValue = TicketResponseStatusEnum | "ALL";

export const TICKET_STATUS_FILTER_OPTIONS: {
  value: TicketStatusFilterValue;
  label: string;
}[] = [
  {
    value: "ALL",
    label: "Alle Status",
  },
  {
    value: TicketResponseStatusEnum.Open,
    label: TICKET_STATUS_LABELS[TicketResponseStatusEnum.Open],
  },
  {
    value: TicketResponseStatusEnum.InProgress,
    label: TICKET_STATUS_LABELS[TicketResponseStatusEnum.InProgress],
  },
  {
    value: TicketResponseStatusEnum.OnHold,
    label: TICKET_STATUS_LABELS[TicketResponseStatusEnum.OnHold],
  },
  {
    value: TicketResponseStatusEnum.Resolved,
    label: TICKET_STATUS_LABELS[TicketResponseStatusEnum.Resolved],
  },
  {
    value: TicketResponseStatusEnum.Closed,
    label: TICKET_STATUS_LABELS[TicketResponseStatusEnum.Closed],
  },
];
