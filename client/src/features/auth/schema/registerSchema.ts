import z from "zod";

export const registerSchema = z.object({
    email: z.string().min(1, "Email eingeben").email("Gültige Email eingeben"),
    firstName: z.string().min(2, "Vorname eingeben"),
    lastName: z.string().min(2, "Nachname eingeben"),
    companyName: z.string().min(2, "Unternehmensname eingeben"),
    password: z.string().min(8, "Mindestens 8 Zeichen"),
    confirmPassword: z.string().min(8, "Mindestens 8 Zeichen"),
}).refine((data) => data.password === data.confirmPassword, {
    message: "Die Passwörter stimmen nicht überein",
    path: ["confirmPassword"],
});

export type RegisterFields = z.infer<typeof registerSchema>;