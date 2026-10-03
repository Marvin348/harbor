import { zodResolver } from "@hookform/resolvers/zod";
import { Plus } from "lucide-react";
import { Controller, useForm } from "react-hook-form";
import { Button } from "@/components/ui/button.tsx";
import { DialogClose, DialogFooter } from "@/components/ui/dialog.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";
import { SlaPolicyPrioritySelect } from "@/features/settings/sla/components/SlaPolicyPrioritySelect.tsx";
import { getCreateSlaPolicyErrorMessage } from "@/features/settings/sla/errors/getCreateSlaPolicyErrorMessage.ts";
import { useCreateSlaPolicy } from "@/features/settings/sla/hooks/useCreateSlaPolicy.ts";
import {
  createSlaPolicySchema,
  type CreateSlaPolicyFields,
} from "@/features/settings/sla/schema/createSlaPolicySchema.ts";

type CreateSlaPolicyFormProps = {
  onCreated: () => void;
};

export const CreateSlaPolicyForm = ({
  onCreated,
}: CreateSlaPolicyFormProps) => {
  const { mutate, isPending } = useCreateSlaPolicy();

  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CreateSlaPolicyFields>({
    resolver: zodResolver(createSlaPolicySchema),
    defaultValues: {
      name: "",
    },
  });

  const onSubmit = (data: CreateSlaPolicyFields) => {
    mutate(data, {
      onSuccess: () => {
        reset();
        showSuccessToast("SLA-Richtlinie wurde erstellt.");
        onCreated();
      },
      onError: (error) => {
        showErrorToast(getCreateSlaPolicyErrorMessage(error));
      },
    });
  };

  return (
    <form className="grid gap-5" onSubmit={handleSubmit(onSubmit)} noValidate>
      <label className="grid gap-2">
        <span className="text-sm font-medium">Name</span>
        <Input
          id="sla-policy-name"
          autoComplete="off"
          placeholder="z. B. Kritische Anfragen"
          aria-invalid={Boolean(errors.name)}
          aria-describedby={errors.name ? "sla-policy-name-error" : undefined}
          {...register("name")}
        />
        {errors.name && (
          <p id="sla-policy-name-error" className="text-xs text-destructive">
            {errors.name.message}
          </p>
        )}
      </label>

      <label className="grid gap-2">
        <span className="text-sm font-medium">Ticket-Priorität</span>
        <Controller
          control={control}
          name="ticketPriority"
          render={({ field }) => (
            <SlaPolicyPrioritySelect
              value={field.value}
              onValueChange={field.onChange}
              ariaInvalid={Boolean(errors.ticketPriority)}
              ariaDescribedBy={
                errors.ticketPriority ? "sla-policy-priority-error" : undefined
              }
            />
          )}
        />
        {errors.ticketPriority && (
          <p
            id="sla-policy-priority-error"
            className="text-xs text-destructive"
          >
            {errors.ticketPriority.message}
          </p>
        )}
      </label>

      <div className="grid gap-4 sm:grid-cols-2">
        <label className="grid gap-2">
          <span className="text-sm font-medium">Reaktionszeit</span>
          <div className="relative">
            <Input
              id="sla-policy-response-time"
              type="number"
              inputMode="numeric"
              min={1}
              step={1}
              placeholder="15"
              className="pr-16"
              aria-invalid={Boolean(errors.responseTimeMinutes)}
              aria-describedby={
                errors.responseTimeMinutes
                  ? "sla-policy-response-time-error"
                  : "sla-policy-response-time-unit"
              }
              {...register("responseTimeMinutes", { valueAsNumber: true })}
            />
            <span
              id="sla-policy-response-time-unit"
              className="pointer-events-none absolute inset-y-0 right-2.5 flex items-center text-xs text-muted-foreground"
            >
              Minuten
            </span>
          </div>
          {errors.responseTimeMinutes && (
            <p
              id="sla-policy-response-time-error"
              className="text-xs text-destructive"
            >
              {errors.responseTimeMinutes.message}
            </p>
          )}
        </label>

        <label className="grid gap-2">
          <span className="text-sm font-medium">Lösungszeit</span>
          <div className="relative">
            <Input
              id="sla-policy-resolution-time"
              type="number"
              inputMode="numeric"
              min={1}
              step={1}
              placeholder="120"
              className="pr-16"
              aria-invalid={Boolean(errors.resolutionTimeMinutes)}
              aria-describedby={
                errors.resolutionTimeMinutes
                  ? "sla-policy-resolution-time-error"
                  : "sla-policy-resolution-time-unit"
              }
              {...register("resolutionTimeMinutes", { valueAsNumber: true })}
            />
            <span
              id="sla-policy-resolution-time-unit"
              className="pointer-events-none absolute inset-y-0 right-2.5 flex items-center text-xs text-muted-foreground"
            >
              Minuten
            </span>
          </div>
          {errors.resolutionTimeMinutes && (
            <p
              id="sla-policy-resolution-time-error"
              className="text-xs text-destructive"
            >
              {errors.resolutionTimeMinutes.message}
            </p>
          )}
        </label>
      </div>

      <p className="text-xs leading-5 text-muted-foreground">
        Die Lösungszeit muss länger als die Reaktionszeit sein. Zeiten werden in
        Minuten angegeben.
      </p>

      <DialogFooter>
        <DialogClose render={<Button type="button" variant="outline" />}>
          Abbrechen
        </DialogClose>
        <Button
          type="submit"
          disabled={isPending || isSubmitting}
          className="sm:min-w-32"
        >
          {isPending ? <Spinner /> : <Plus />}
          Erstellen
        </Button>
      </DialogFooter>
    </form>
  );
};
