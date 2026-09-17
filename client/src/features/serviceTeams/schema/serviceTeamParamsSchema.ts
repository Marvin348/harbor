import z from "zod";

export const serviceTeamParamsSchema = z.object({
  page: z.coerce.number().int().min(1).catch(1),
  search: z.string().trim().optional().catch(undefined),
});

export type ServiceTeamParams = z.infer<typeof serviceTeamParamsSchema>;
