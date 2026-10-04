import z from "zod";

export const updateSlaPolicySchema = z
  .object({
    name: z
      .string()
      .trim()
      .min(1, "Name der SLA-Richtlinie eingeben")
      .max(100, "Name darf maximal 100 Zeichen lang sein")
      .optional(),
    responseTimeMinutes: z
      .number({ message: "Reaktionszeit eingeben" })
      .int("Reaktionszeit muss eine ganze Zahl sein")
      .positive("Reaktionszeit muss größer als 0 sein")
      .optional(),
    resolutionTimeMinutes: z
      .number({ message: "Lösungszeit eingeben" })
      .int("Lösungszeit muss eine ganze Zahl sein")
      .positive("Lösungszeit muss größer als 0 sein")
      .optional(),
  })
  .refine(
    ({ responseTimeMinutes, resolutionTimeMinutes }) =>
      responseTimeMinutes === undefined ||
      resolutionTimeMinutes === undefined ||
      resolutionTimeMinutes > responseTimeMinutes,
    {
      path: ["resolutionTimeMinutes"],
      message: "Lösungszeit muss größer als die Reaktionszeit sein",
    },
  );

export type UpdateSlaPolicyFields = z.infer<typeof updateSlaPolicySchema>;
