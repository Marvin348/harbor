import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema } from "@/features/auth/schema/loginSchema.ts";
import type { LoginFields } from "@/features/auth/schema/loginSchema.ts";
import { useLogin } from "@/features/auth/hooks/useLogin.ts";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { Building2, CheckCircle2 } from "lucide-react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

export const LoginPage = () => {
  const { mutate, isPending, isError } = useLogin();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFields>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = (data: LoginFields) => {
    mutate(data, {
      onSuccess: () => {
        navigate("/overview");
      },
    });
  };

  return (
    <main className="min-h-screen bg-background px-4 py-10 text-foreground sm:px-6 lg:px-8">
      <div className="mx-auto flex min-h-[calc(100vh-5rem)] w-full max-w-5xl items-center justify-center">
        <section className="grid w-full overflow-hidden rounded-lg border border-border bg-card shadow-sm lg:grid-cols-[0.82fr_1.18fr]">
          <div className="flex flex-col justify-between border-b border-border bg-muted/30 p-6 sm:p-8 lg:border-r lg:border-b-0">
            <div>
              <div className="mb-10 flex items-center gap-3">
                <div className="flex size-8 items-center justify-center rounded-md border border-border bg-background">
                  <Building2 className="size-4" aria-hidden="true" />
                </div>
                <div>
                  <p className="text-sm font-semibold leading-none">Harbor</p>
                  <p className="mt-1 text-xs text-muted-foreground">
                    Service Management
                  </p>
                </div>
              </div>

              <p className="text-xs font-medium uppercase text-muted-foreground">
                Harbor-Zugang
              </p>
              <h1 className="mt-3 max-w-sm text-2xl font-semibold tracking-normal text-foreground">
                Bei Harbor anmelden
              </h1>
              <p className="mt-3 max-w-sm text-sm leading-6 text-muted-foreground">
                Melde dich mit deinem Account an und verwalte deine Organisation
                weiter.
              </p>
            </div>

            <div className="mt-10 border-t border-border pt-6">
              <div className="flex items-start gap-3">
                <CheckCircle2
                  className="mt-0.5 size-4 shrink-0 text-muted-foreground"
                  aria-hidden="true"
                />
                <p className="text-xs leading-5 text-muted-foreground">
                  Nutze die E-Mail-Adresse, mit der du zu deiner Organisation
                  eingeladen wurdest.
                </p>
              </div>
            </div>
          </div>

          <div className="p-6 sm:p-8">
            <div className="mb-7">
              <h2 className="text-xl font-semibold tracking-normal">Login</h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Willkommen zurück. Melde dich mit deinem Admin- oder
                Team-Account an.
              </p>
            </div>

            <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
              <div className="space-y-2">
                <label className="text-sm font-medium" htmlFor="login-email">
                  Geschäftliche E-Mail
                </label>
                <Input
                  id="login-email"
                  type="email"
                  autoComplete="email"
                  placeholder="name@unternehmen.de"
                  aria-invalid={Boolean(errors.email)}
                  aria-describedby={
                    errors.email ? "login-email-error" : undefined
                  }
                  {...register("email")}
                />
                {errors.email && (
                  <p
                    id="login-email-error"
                    className="text-xs text-destructive"
                  >
                    {errors.email.message}
                  </p>
                )}
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium" htmlFor="login-password">
                  Passwort
                </label>
                <Input
                  id="login-password"
                  type="password"
                  autoComplete="current-password"
                  placeholder="Passwort eingeben"
                  aria-invalid={Boolean(errors.password)}
                  aria-describedby={
                    errors.password ? "login-password-error" : undefined
                  }
                  {...register("password")}
                />
                {errors.password && (
                  <p
                    id="login-password-error"
                    className="text-xs text-destructive"
                  >
                    {errors.password.message}
                  </p>
                )}
              </div>

              {isError && (
                <p className="text-xs text-destructive">
                  Anmeldung fehlgeschlagen. Bitte prüfe deine Zugangsdaten.
                </p>
              )}

              <Button
                className="mt-2 w-full"
                disabled={isSubmitting || isPending}
                size="lg"
                type="submit"
              >
                {isPending ? <Spinner /> : "Anmelden"}
              </Button>
            </form>

            <p className="mt-6 text-center text-sm text-muted-foreground">
              Noch keine Organisation?{" "}
              <Link
                className="font-medium text-foreground underline-offset-4 hover:underline"
                to="/register"
              >
                Organisation erstellen
              </Link>
            </p>
          </div>
        </section>
      </div>
    </main>
  );
};
