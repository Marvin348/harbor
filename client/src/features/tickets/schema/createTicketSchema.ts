import z from "zod";
import {
  TicketAssessmentRequestBusinessCriticalityEnum,
  TicketAssessmentRequestImpactEnum,
  TicketAssessmentRequestUrgencyEnum,
} from "@/api/generated/models/ticket-assessment-request.ts";

export const createTicketSchema = z.object({
  serviceId: z.number({
    message: "Bitte einen Service auswählen",
  }),
  subject: z
    .string()
    .trim()
    .min(1, "Betreff eingeben")
    .max(150, "Betreff darf maximal 150 Zeichen lang sein"),
  description: z
    .string()
    .trim()
    .min(1, "Beschreibung eingeben")
    .max(1500, "Beschreibung darf maximal 1500 Zeichen lang sein"),
  assessment: z.object({
    impact: z.enum(
      [
        TicketAssessmentRequestImpactEnum.SingleUser,
        TicketAssessmentRequestImpactEnum.Team,
        TicketAssessmentRequestImpactEnum.MultipleTeams,
        TicketAssessmentRequestImpactEnum.Organization,
      ],
      {
        message: "Bitte Auswirkung auswählen",
      },
    ),
    urgency: z.enum(
      [
        TicketAssessmentRequestUrgencyEnum.WorkAroundAvailable,
        TicketAssessmentRequestUrgencyEnum.WorkDegraded,
        TicketAssessmentRequestUrgencyEnum.WorkBlocked,
      ],
      {
        message: "Bitte Dringlichkeit auswählen",
      },
    ),
    businessCriticality: z.enum(
      [
        TicketAssessmentRequestBusinessCriticalityEnum.Low,
        TicketAssessmentRequestBusinessCriticalityEnum.Normal,
        TicketAssessmentRequestBusinessCriticalityEnum.High,
        TicketAssessmentRequestBusinessCriticalityEnum.MissionCritical,
      ],
      {
        message: "Bitte Geschäftskritikalität auswählen",
      },
    ),
  }),
});

export type CreateTicketFields = z.infer<typeof createTicketSchema>;
