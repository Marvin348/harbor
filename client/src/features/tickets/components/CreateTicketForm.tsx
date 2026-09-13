import { zodResolver } from "@hookform/resolvers/zod";
import { Plus } from "lucide-react";
import { Controller, useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import { DialogClose, DialogFooter } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  createTicketSchema,
  type CreateTicketFields,
} from "@/features/tickets/schema/createTicketSchema.ts";
import { SelectService } from "@/features/services/components/select/SelectService.tsx";
import { SelectTicketBusinessCriticality } from "@/features/tickets/components/select/SelectTicketBusinessCriticality.tsx";
import { SelectTicketImpact } from "@/features/tickets/components/select/SelectTicketImpact.tsx";
import { SelectTicketUrgency } from "@/features/tickets/components/select/SelectTicketUrgency.tsx";
import { useCreateTicket } from "@/features/tickets/hooks/useCreateTicket.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { getCreateTicketErrorMessage } from "@/features/tickets/errors/getCreateTicketErrorMessage.ts";
import { Spinner } from "@/components/ui/spinner.tsx";

type CreateTicketFormProps = {
  onCreated: () => void;
};

export const CreateTicketForm = ({ onCreated }: CreateTicketFormProps) => {
  const { mutate, isPending } = useCreateTicket();

  const {
    control,
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateTicketFields>({
    resolver: zodResolver(createTicketSchema),
    defaultValues: {
      subject: "",
      description: "",
    },
  });

  const onSubmit = (data: CreateTicketFields) => {
    mutate(data, {
      onSuccess: () => {
        showSuccessToast("Anfrage wurde erstellt.");
        onCreated();
      },

      onError: (error) => {
        showErrorToast(getCreateTicketErrorMessage(error));
      },
    });
  };

  const hasAssessmentError = Boolean(
    errors.assessment?.impact ||
    errors.assessment?.urgency ||
    errors.assessment?.businessCriticality,
  );

  return (
    <form className="grid gap-3" onSubmit={handleSubmit(onSubmit)}>
      <section className="grid gap-3">
        <div className="space-y-0.5">
          <h3 className="text-base font-semibold">Worum geht es?</h3>
          <p className="text-xs text-muted-foreground">
            Betreff, passender Service und eine kurze Beschreibung.
          </p>
        </div>

        <label className="grid gap-2">
          <span className="text-sm font-medium">Betreff</span>
          <Input
            id="ticket-subject"
            autoComplete="off"
            aria-invalid={Boolean(errors.subject)}
            aria-describedby={
              errors.subject ? "ticket-subject-error" : undefined
            }
            {...register("subject")}
          />
          {errors.subject && (
            <p id="ticket-subject-error" className="text-xs text-destructive">
              {errors.subject.message}
            </p>
          )}
        </label>

        <label className="grid gap-2">
          <span className="text-sm font-medium">Service</span>
          <Controller
            control={control}
            name="serviceId"
            render={({ field }) => (
              <SelectService
                value={field.value}
                onValueChange={field.onChange}
                ariaInvalid={Boolean(errors.serviceId)}
                ariaDescribedBy={
                  errors.serviceId ? "serviceId-error" : undefined
                }
              />
            )}
          />

          {errors.serviceId && (
            <p id="ticket-service-error" className="text-xs text-destructive">
              {errors.serviceId.message}
            </p>
          )}
        </label>

        <label className="grid gap-2">
          <span className="text-sm font-medium">Beschreibung</span>
          <Textarea
            id="ticket-description"
            className="min-h-24 resize-none"
            aria-invalid={Boolean(errors.description)}
            aria-describedby={
              errors.description ? "ticket-description-error" : undefined
            }
            {...register("description")}
          />
          {errors.description && (
            <p
              id="ticket-description-error"
              className="text-xs text-destructive"
            >
              {errors.description.message}
            </p>
          )}
        </label>
      </section>

      <section className="grid gap-3 pt-3">
        <div className="space-y-0.5">
          <h3 className="text-base font-semibold">Priorisierung</h3>
          <p className="text-xs text-muted-foreground">
            Hilft dem Service-Team, die Anfrage einzuordnen.
          </p>
        </div>

        <div className="grid gap-3 md:grid-cols-2">
          <label className="grid gap-2">
            <span className="text-sm font-medium">Auswirkung</span>
            <Controller
              control={control}
              name="assessment.impact"
              render={({ field }) => (
                <SelectTicketImpact
                  value={field.value}
                  onValueChange={field.onChange}
                  ariaInvalid={Boolean(errors.assessment?.impact)}
                  ariaDescribedBy={
                    hasAssessmentError ? "ticket-assessment-error" : undefined
                  }
                />
              )}
            />
          </label>

          <label className="grid gap-2">
            <span className="text-sm font-medium">Dringlichkeit</span>
            <Controller
              control={control}
              name="assessment.urgency"
              render={({ field }) => (
                <SelectTicketUrgency
                  value={field.value}
                  onValueChange={field.onChange}
                  ariaInvalid={Boolean(errors.assessment?.urgency)}
                  ariaDescribedBy={
                    hasAssessmentError ? "ticket-assessment-error" : undefined
                  }
                />
              )}
            />
          </label>

          <label className="grid gap-2">
            <span className="text-sm font-medium">Geschäftliche Bedeutung</span>
            <Controller
              control={control}
              name="assessment.businessCriticality"
              render={({ field }) => (
                <SelectTicketBusinessCriticality
                  value={field.value}
                  onValueChange={field.onChange}
                  ariaInvalid={Boolean(errors.assessment?.businessCriticality)}
                  ariaDescribedBy={
                    hasAssessmentError ? "ticket-assessment-error" : undefined
                  }
                />
              )}
            />

            {hasAssessmentError && (
              <p className="text-xs text-destructive">
                Bitte Priorisierung vollständig ausfüllen.
              </p>
            )}
          </label>
        </div>
      </section>

      <DialogFooter>
        <DialogClose render={<Button type="button" variant="outline" />}>
          Abbrechen
        </DialogClose>
        <Button
          type="submit"
          className="sm:w-36"
          disabled={isPending || isSubmitting}
        >
          {isPending ? <Spinner /> : <Plus />}
          Erstellen
        </Button>
      </DialogFooter>
    </form>
  );
};
