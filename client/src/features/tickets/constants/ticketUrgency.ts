import { TicketAssessmentRequestUrgencyEnum } from "@/api/generated/models/ticket-assessment-request.ts";

export const urgencyOptions = [
  {
    value: TicketAssessmentRequestUrgencyEnum.WorkAroundAvailable,
    label: "Workaround vorhanden",
  },
  {
    value: TicketAssessmentRequestUrgencyEnum.WorkDegraded,
    label: "Arbeit eingeschränkt",
  },
  {
    value: TicketAssessmentRequestUrgencyEnum.WorkBlocked,
    label: "Arbeit nicht möglich",
  },
];

export const URGENCY_LABELS: Record<
  TicketAssessmentRequestUrgencyEnum,
  string
> = {
  [TicketAssessmentRequestUrgencyEnum.WorkAroundAvailable]:
    "Workaround vorhanden",
  [TicketAssessmentRequestUrgencyEnum.WorkDegraded]: "Arbeit eingeschränkt",
  [TicketAssessmentRequestUrgencyEnum.WorkBlocked]: "Arbeit nicht möglich",
};
