import z from "zod";

export const createServiceSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, "Servicename eingeben")
    .max(100, "Servicename darf maximal 100 Zeichen lang sein"),
  description: z
    .string()
    .trim()
    .max(250, "Beschreibung darf maximal 250 Zeichen lang sein")
    .optional(),

  serviceTeamId: z.number({
    message: "Bitte ein Service-Team auswählen",
  }),
});

export type CreateServiceFields = z.infer<typeof createServiceSchema>;
