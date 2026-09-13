import { TicketAssessmentRequestImpactEnum } from "@/api/generated/models/ticket-assessment-request.ts";

export const impactOptions = [
  {
    value: TicketAssessmentRequestImpactEnum.SingleUser,
    label: "Nur ich",
  },
  {
    value: TicketAssessmentRequestImpactEnum.Team,
    label: "Mein Team",
  },
  {
    value: TicketAssessmentRequestImpactEnum.MultipleTeams,
    label: "Mehrere Teams",
  },
  {
    value: TicketAssessmentRequestImpactEnum.Organization,
    label: "Ganze Organisation",
  },
] as const;

export const IMPACT_LABELS: Record<TicketAssessmentRequestImpactEnum, string> =
  {
    [TicketAssessmentRequestImpactEnum.SingleUser]: "Nur ich",
    [TicketAssessmentRequestImpactEnum.Team]: "Mein Team",
    [TicketAssessmentRequestImpactEnum.MultipleTeams]: "Mehrere Teams",
    [TicketAssessmentRequestImpactEnum.Organization]: "Ganze Organisation",
  };
