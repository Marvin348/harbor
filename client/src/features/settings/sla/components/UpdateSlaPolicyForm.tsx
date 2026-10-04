import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";
import { Button } from "@/components/ui/button.tsx";
import { DialogClose, DialogFooter } from "@/components/ui/dialog.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { SlaPolicyPrioritySelect } from "@/features/settings/sla/components/SlaPolicyPrioritySelect.tsx";
import { getUpdateSlaPolicyErrorMessage } from "@/features/settings/sla/errors/getUpdateSlaPolicyErrorMessage.ts";
import { useUpdateSlaPolicy } from "@/features/settings/sla/hooks/useUpdateSlaPolicy.ts";
import {
  updateSlaPolicySchema,
  type UpdateSlaPolicyFields,
} from "@/features/settings/sla/schema/updateSlaPolicySchema.ts";

type UpdateSlaPolicyFormProps = {
  policy: SlaPolicyResponse;
  onUpdated: () => void;
};

export const UpdateSlaPolicyForm = ({
  policy,
  onUpdated,
}: UpdateSlaPolicyFormProps) => {
  const { mutate, isPending } = useUpdateSlaPolicy();

  const {
    register,
    handleSubmit,
    formState: { dirtyFields, errors, isDirty, isSubmitting },
  } = useForm<UpdateSlaPolicyFields>({
    resolver: zodResolver(updateSlaPolicySchema),
    defaultValues: {
      name: policy.name,
      responseTimeMinutes: policy.responseTimeMinutes,
      resolutionTimeMinutes: policy.resolutionTimeMinutes,
    },
  });

  const onSubmit = (data: UpdateSlaPolicyFields) => {
    const changes: UpdateSlaPolicyFields = {};

    if (dirtyFields.name) {
      changes.name = data.name;
    }
    if (dirtyFields.responseTimeMinutes) {
      changes.responseTimeMinutes = data.responseTimeMinutes;
    }
    if (dirtyFields.resolutionTimeMinutes) {
      changes.resolutionTimeMinutes = data.resolutionTimeMinutes;
    }

    mutate(
      { id: policy.id, data: changes },
      {
        onSuccess: () => {
          showSuccessToast("SLA-Richtlinie wurde aktualisiert.");
          onUpdated();
        },
        onError: (error) => {
          showErrorToast(getUpdateSlaPolicyErrorMessage(error));
        },
      },
    );
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
        <SlaPolicyPrioritySelect value={policy.ticketPriority} disabled />
        <p className="text-xs text-muted-foreground">
          Die Priorität einer bestehenden Richtlinie kann nicht geändert werden.
        </p>
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
          disabled={!isDirty || isPending || isSubmitting}
          className="sm:min-w-32"
        >
          {isPending && <Spinner />}
          Speichern
        </Button>
      </DialogFooter>
    </form>
  );
};
