import z from "zod";
import { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";

export const createSlaPolicySchema = z
  .object({
    name: z
      .string()
      .trim()
      .min(1, "Name der SLA-Richtlinie eingeben")
      .max(100, "Name darf maximal 100 Zeichen lang sein"),
    ticketPriority: z.enum(
      [
        TicketResponsePriorityEnum.Low,
        TicketResponsePriorityEnum.Medium,
        TicketResponsePriorityEnum.High,
        TicketResponsePriorityEnum.Critical,
      ],
      {
        message: "Bitte eine Priorität auswählen",
      },
    ),
    responseTimeMinutes: z
      .number({ message: "Reaktionszeit eingeben" })
      .int("Reaktionszeit muss eine ganze Zahl sein")
      .positive("Reaktionszeit muss größer als 0 sein"),
    resolutionTimeMinutes: z
      .number({ message: "Lösungszeit eingeben" })
      .int("Lösungszeit muss eine ganze Zahl sein")
      .positive("Lösungszeit muss größer als 0 sein"),
  })
  .refine(
    ({ responseTimeMinutes, resolutionTimeMinutes }) =>
      resolutionTimeMinutes > responseTimeMinutes,
    {
      path: ["resolutionTimeMinutes"],
      message: "Lösungszeit muss größer als die Reaktionszeit sein",
    },
  );

export type CreateSlaPolicyFields = z.infer<typeof createSlaPolicySchema>;
