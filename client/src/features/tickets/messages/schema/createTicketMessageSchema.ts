import z from "zod";
import {
  CreateTicketMessageRequestTypeEnum,
  type CreateTicketMessageRequest,
} from "@/api/generated/models/create-ticket-message-request.ts";

export const createTicketMessageSchema = z
  .strictObject({
    type: z.enum(CreateTicketMessageRequestTypeEnum, {
      message: "Bitte einen Nachrichtentyp auswählen",
    }),
    body: z
      .string({ message: "Nachricht eingeben" })
      .trim()
      .min(1, "Nachricht eingeben"),
  }) satisfies z.ZodType<CreateTicketMessageRequest>;

export type CreateTicketMessageFields = z.infer<
  typeof createTicketMessageSchema
>;
