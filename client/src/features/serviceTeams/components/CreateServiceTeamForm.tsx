import { Input } from "@/components/ui/input.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";
import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { ServiceTeamTemplatePicker } from "@/features/serviceTeams/components/ServiceTeamTemplatePicker.tsx";
import { SheetClose } from "@/components/ui/sheet.tsx";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  createServiceTeamSchema,
  type CreateServiceTeamFields,
} from "@/features/serviceTeams/schema/createServiceTeamSchema.ts";
import { useCreateServiceTeam } from "@/features/serviceTeams/hooks/useCreateServiceTeam.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";
import { getCreateServiceTeamErrorMessage } from "@/features/serviceTeams/errors/getCreateServiceTeamErrorMessage.ts";

type CreateServiceTeamFormProps = {
  onClosePanel: () => void;
};

export const CreateServiceTeamForm = ({
  onClosePanel,
}: CreateServiceTeamFormProps) => {
  const { mutate, isPending } = useCreateServiceTeam();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateServiceTeamFields>({
    resolver: zodResolver(createServiceTeamSchema),
    defaultValues: {
      name: "",
      description: "",
    },
  });

  const onSubmit = (data: CreateServiceTeamFields) => {
    mutate(data, {
      onSuccess: () => {
        showSuccessToast("Service-Team wurde erstellt.");
        onClosePanel();
      },

      onError: (error) => {
        showErrorToast(getCreateServiceTeamErrorMessage(error));
      },
    });
  };

  return (
    <form
      className="flex h-full min-h-0 flex-col"
      onSubmit={handleSubmit(onSubmit)}
    >
      <div className="min-h-0 flex-1 space-y-4 overflow-y-auto px-5 py-4 [-ms-overflow-style:none] [scrollbar-width:none] [&::-webkit-scrollbar]:hidden">
        <label className="grid gap-2">
          <span className="text-sm font-medium">Teamname</span>
          <Input
            id="service-team-name"
            placeholder="z. B. IT Support"
            aria-invalid={Boolean(errors.name)}
            aria-describedby={
              errors.name ? "service-team-name-error" : undefined
            }
            {...register("name")}
          />
          {errors.name && (
            <p
              id="service-team-name-error"
              className="text-xs text-destructive"
            >
              {errors.name.message}
            </p>
          )}
        </label>

        <label className="grid gap-2">
          <span className="text-sm font-medium">Beschreibung</span>
          <Textarea
            id="service-team-description"
            className="min-h-24 resize-none"
            placeholder="Wofür ist dieses Service-Team zuständig?"
            aria-invalid={Boolean(errors.description)}
            aria-describedby={
              errors.description ? "service-team-description-error" : undefined
            }
            {...register("description")}
          />
          {errors.description && (
            <p
              id="service-team-description-error"
              className="text-xs text-destructive"
            >
              {errors.description.message}
            </p>
          )}
        </label>

        <ServiceTeamTemplatePicker />
      </div>

      <div className="shrink-0 border-t border-border px-5 py-4">
        <div className="flex justify-end gap-2">
          <SheetClose render={<Button type="button" variant="outline" />}>
            Abbrechen
          </SheetClose>
          <Button type="submit" disabled={isSubmitting || isPending}>
            {isPending ? <Spinner /> : <Plus />}
            Erstellen
          </Button>
        </div>
      </div>
    </form>
  );
};
