import z from "zod";
import { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";
import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const serviceTeamTicketParamsSchema = z.object({
  page: z.coerce.number().int().min(1).catch(1),
  search: z.string().trim().optional().catch(undefined),
  priority: z.enum(TicketResponsePriorityEnum).optional(),
  status: z.enum(TicketResponseStatusEnum).optional(),
});

export type serviceTeamTicketParams = z.infer<
  typeof serviceTeamTicketParamsSchema
>;
