import { zodResolver } from "@hookform/resolvers/zod";
import { Plus } from "lucide-react";
import { Controller, useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import { DialogClose, DialogFooter } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { ServiceTeamSelect } from "@/features/serviceTeams/components/ServiceTeamSelect";
import {
  createServiceSchema,
  type CreateServiceFields,
} from "@/features/services/schema/createServiceSchema";
import { useCreateService } from "@/features/services/hooks/useCreateService.ts";
import { Spinner } from "@/components/ui/spinner.tsx";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { getCreateServiceErrorMessage } from "@/features/services/errors/getCreateServiceErrorMessage.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";

type CreateServiceFormProps = {
  onCreated: () => void;
};

export const CreateServiceForm = ({ onCreated }: CreateServiceFormProps) => {
  const { mutate, isPending } = useCreateService();

  const {
    control,
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateServiceFields>({
    resolver: zodResolver(createServiceSchema),
    defaultValues: {
      name: "",
      description: "",
    },
  });

  const onSubmit = (data: CreateServiceFields) => {
    mutate(data, {
      onSuccess: () => {
        showSuccessToast("Service wurde erstellt.");
        onCreated();
      },

      onError: (error) => {
        showErrorToast(getCreateServiceErrorMessage(error));
      },
    });
  };

  return (
    <form className="grid gap-4" onSubmit={handleSubmit(onSubmit)}>
      <label className="grid gap-2">
        <span className="text-sm font-medium">Name</span>
        <Input
          id="service-name"
          autoComplete="off"
          aria-invalid={Boolean(errors.name)}
          aria-describedby={errors.name ? "service-name-error" : undefined}
          {...register("name")}
        />
        {errors.name && (
          <p id="service-name-error" className="text-xs text-destructive">
            {errors.name.message}
          </p>
        )}
      </label>

      <label className="grid gap-2">
        <span className="text-sm font-medium">Beschreibung</span>
        <Textarea
          id="service-description"
          className="min-h-24 resize-none"
          aria-invalid={Boolean(errors.description)}
          aria-describedby={
            errors.description ? "service-description-error" : undefined
          }
          {...register("description")}
        />
        {errors.description && (
          <p
            id="service-description-error"
            className="text-xs text-destructive"
          >
            {errors.description.message}
          </p>
        )}
      </label>

      <label className="grid gap-2">
        <span className="text-sm font-medium">Service-Team</span>

        <Controller
          control={control}
          name="serviceTeamId"
          render={({ field }) => (
            <ServiceTeamSelect
              value={field.value}
              onValueChange={field.onChange}
              ariaInvalid={Boolean(errors.serviceTeamId)}
              ariaDescribedBy={
                errors.serviceTeamId ? "service-team-error" : undefined
              }
            />
          )}
        />
        {errors.serviceTeamId && (
          <p id="service-team-error" className="text-xs text-destructive">
            {errors.serviceTeamId.message}
          </p>
        )}
      </label>

      <DialogFooter>
        <DialogClose render={<Button type="button" variant="outline" />}>
          Abbrechen
        </DialogClose>
        <Button
          type="submit"
          className="sm:w-30"
          disabled={isPending || isSubmitting}
        >
          {isPending ? <Spinner /> : <Plus />}
          Erstellen
        </Button>
      </DialogFooter>
    </form>
  );
};
