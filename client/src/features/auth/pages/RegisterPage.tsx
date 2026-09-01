import { Building2, ShieldCheck } from "lucide-react";
import {
  type RegisterFields,
  registerSchema,
} from "@/features/auth/schema/registerSchema.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useForm } from "react-hook-form";
import { useRegister } from "@/features/auth/hooks/useRegister.ts";
import { Spinner } from "@/components/ui/spinner.tsx";
import { getRegisterErrorMessage } from "@/features/auth/utils/getRegisterErrorMessage.ts";
import { useNavigate } from "react-router-dom";

export const RegisterPage = () => {
  const { mutate, isPending, error } = useRegister();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFields>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      email: "",
      firstName: "",
      lastName: "",
      companyName: "",
      password: "",
      confirmPassword: "",
    },
  });

  const onSubmit = (data: RegisterFields) => {
    mutate(data, {
      onSuccess: () => {
        navigate("/login");
      },
    });
  };

  const errorMessage = error ? getRegisterErrorMessage(error) : null;

  return (
    <main className="min-h-screen bg-background px-4 py-10 text-foreground sm:px-6 lg:px-8">
      <div className="mx-auto flex min-h-[calc(100vh-5rem)] w-full max-w-5xl items-center justify-center">
        <section className="grid w-full overflow-hidden rounded-lg border border-border bg-card shadow-sm lg:grid-cols-[0.95fr_1.05fr]">
          <div className="flex flex-col justify-between border-b border-border bg-muted/40 p-6 sm:p-8 lg:border-r lg:border-b-0">
            <div>
              <div className="mb-6 flex size-10 items-center justify-center rounded-lg border border-border bg-background">
                <Building2 className="size-5" aria-hidden="true" />
              </div>

              <p className="mb-3 text-sm font-medium text-muted-foreground">
                Harbor Service Management
              </p>
              <h1 className="max-w-sm text-3xl font-semibold tracking-normal text-foreground">
                Organisation erstellen
              </h1>
              <p className="mt-4 max-w-sm text-sm leading-6 text-muted-foreground">
                Richte einen neuen Harbor-Arbeitsbereich für dein Unternehmen,
                deine Service-Teams und Enterprise-Workflows ein.
              </p>
            </div>

            <div className="mt-10 flex items-start gap-3 rounded-lg border border-border bg-background p-4">
              <ShieldCheck
                className="mt-0.5 size-4 shrink-0 text-muted-foreground"
                aria-hidden="true"
              />
              <p className="text-xs leading-5 text-muted-foreground">
                Du wirst der erste Organisationsadmin. Dein Team kannst du nach
                der Einrichtung einladen.
              </p>
            </div>
          </div>

          <div className="p-6 sm:p-8">
            <div className="mb-7">
              <h2 className="text-xl font-semibold tracking-normal">
                Angaben zur Organisation
              </h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Lege fest, wer den ersten Admin-Account verwaltet.
              </p>
            </div>

            <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
              <div className="space-y-2">
                <label className="text-sm font-medium" htmlFor="work-email">
                  Geschäftliche E-Mail
                </label>
                <Input
                  id="work-email"
                  type="email"
                  autoComplete="email"
                  placeholder="name@unternehmen.de"
                  aria-invalid={Boolean(errors.email)}
                  aria-describedby={
                    errors.email ? "work-email-error" : undefined
                  }
                  {...register("email")}
                />
                {errors.email ? (
                  <p id="work-email-error" className="text-xs text-destructive">
                    {errors.email.message}
                  </p>
                ) : null}
              </div>

              <div className="grid gap-5 sm:grid-cols-2">
                <div className="space-y-2">
                  <label className="text-sm font-medium" htmlFor="first-name">
                    Vorname
                  </label>
                  <Input
                    id="first-name"
                    autoComplete="given-name"
                    placeholder="Alex"
                    aria-invalid={Boolean(errors.firstName)}
                    aria-describedby={
                      errors.firstName ? "first-name-error" : undefined
                    }
                    {...register("firstName")}
                  />
                  {errors.firstName && (
                    <p
                      id="first-name-error"
                      className="text-xs text-destructive"
                    >
                      {errors.firstName.message}
                    </p>
                  )}
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium" htmlFor="last-name">
                    Nachname
                  </label>
                  <Input
                    id="last-name"
                    autoComplete="family-name"
                    placeholder="Morgan"
                    aria-invalid={Boolean(errors.lastName)}
                    aria-describedby={
                      errors.lastName ? "last-name-error" : undefined
                    }
                    {...register("lastName")}
                  />
                  {errors.lastName && (
                    <p
                      id="last-name-error"
                      className="text-xs text-destructive"
                    >
                      {errors.lastName.message}
                    </p>
                  )}
                </div>
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium" htmlFor="company-name">
                  Unternehmensname
                </label>
                <Input
                  id="company-name"
                  autoComplete="organization"
                  placeholder="Acme Operations GmbH"
                  aria-invalid={Boolean(errors.companyName)}
                  aria-describedby={
                    errors.companyName ? "company-name-error" : undefined
                  }
                  {...register("companyName")}
                />
                {errors.companyName && (
                  <p
                    id="company-name-error"
                    className="text-xs text-destructive"
                  >
                    {errors.companyName.message}
                  </p>
                )}
              </div>

              <div className="grid gap-5 sm:grid-cols-2">
                <div className="space-y-2">
                  <label className="text-sm font-medium" htmlFor="password">
                    Passwort
                  </label>
                  <Input
                    id="password"
                    type="password"
                    autoComplete="new-password"
                    placeholder="Passwort erstellen"
                    aria-invalid={Boolean(errors.password)}
                    aria-describedby={
                      errors.password ? "password-error" : undefined
                    }
                    {...register("password")}
                  />
                  {errors.password && (
                    <p id="password-error" className="text-xs text-destructive">
                      {errors.password.message}
                    </p>
                  )}
                </div>

                <div className="space-y-2">
                  <label
                    className="text-sm font-medium"
                    htmlFor="confirm-password"
                  >
                    Passwort bestätigen
                  </label>
                  <Input
                    id="confirm-password"
                    type="password"
                    autoComplete="new-password"
                    placeholder="Passwort bestätigen"
                    aria-invalid={Boolean(errors.confirmPassword)}
                    aria-describedby={
                      errors.confirmPassword
                        ? "confirm-password-error"
                        : undefined
                    }
                    {...register("confirmPassword")}
                  />
                  {errors.confirmPassword && (
                    <p
                      id="confirm-password-error"
                      className="text-xs text-destructive"
                    >
                      {errors.confirmPassword.message}
                    </p>
                  )}
                </div>
              </div>

              {errorMessage && (
                <p className="text-xs text-destructive">{errorMessage}</p>
              )}

              <Button
                className="mt-2 w-full"
                disabled={isSubmitting || isPending}
                size="lg"
                type="submit"
              >
                {isPending ? <Spinner /> : "Organisation erstellen"}
              </Button>
            </form>
          </div>
        </section>
      </div>
    </main>
  );
};
