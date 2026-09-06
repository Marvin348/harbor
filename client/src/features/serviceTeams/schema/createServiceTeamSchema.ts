import z from "zod";

export const createServiceTeamSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, "Teamname eingeben")
    .max(100, "Teamname darf maximal 100 Zeichen lang sein"),
  description: z
    .string()
    .trim()
    .min(1, "Beschreibung eingeben")
    .max(250, "Beschreibung darf maximal 250 Zeichen lang sein"),
});

export type CreateServiceTeamFields = z.infer<typeof createServiceTeamSchema>;
