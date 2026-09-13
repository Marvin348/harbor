import { TicketAssessmentRequestBusinessCriticalityEnum } from "@/api/generated/models/ticket-assessment-request.ts";

export const businessCriticalityOptions = [
  {
    value: TicketAssessmentRequestBusinessCriticalityEnum.Low,
    label: "Niedrig",
  },
  {
    value: TicketAssessmentRequestBusinessCriticalityEnum.Normal,
    label: "Normal",
  },
  {
    value: TicketAssessmentRequestBusinessCriticalityEnum.High,
    label: "Hoch",
  },
  {
    value: TicketAssessmentRequestBusinessCriticalityEnum.MissionCritical,
    label: "Kritisch",
  },
];

export const BUSINESS_CRITICALITY_LABELS: Record<
  TicketAssessmentRequestBusinessCriticalityEnum,
  string
> = {
  [TicketAssessmentRequestBusinessCriticalityEnum.Low]: "Niedrig",
  [TicketAssessmentRequestBusinessCriticalityEnum.Normal]: "Normal",
  [TicketAssessmentRequestBusinessCriticalityEnum.High]: "Hoch",
  [TicketAssessmentRequestBusinessCriticalityEnum.MissionCritical]: "Kritisch",
};
