import { z } from "zod";
import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const requesterTicketParamsSchema = z.object({
  page: z.coerce.number().int().min(1).catch(1),
  search: z.string().trim().optional().catch(undefined),
  status: z.enum(TicketResponseStatusEnum).optional().catch(undefined),
});

export type RequesterTicketParams = z.infer<typeof requesterTicketParamsSchema>;
