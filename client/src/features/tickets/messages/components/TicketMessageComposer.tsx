import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, useWatch } from "react-hook-form";
import { Textarea } from "@/components/ui/textarea.tsx";
import { TicketComposerActionSelector } from "@/features/tickets/messages/components/TicketComposerActionSelector.tsx";
import {
  createTicketMessageSchema,
  type CreateTicketMessageFields,
} from "@/features/tickets/messages/schema/createTicketMessageSchema.ts";
import { CreateTicketMessageRequestTypeEnum } from "@/api/generated/models/create-ticket-message-request.ts";
import { useCreateTicketMessage } from "@/features/tickets/messages/hooks/useCreateTicketMessage.ts";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { createTicketMessageErrorMessage } from "@/features/tickets/messages/errors/createTicketMessageErrorMessage.ts";

type AgentTicketMessageComposerProps = {
  ticketId: number;
};

export const TicketMessageComposer = ({
  ticketId,
}: AgentTicketMessageComposerProps) => {
  const { mutate: createTicketMessage, isPending } = useCreateTicketMessage();

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    control,
    formState: { errors, isSubmitting },
  } = useForm<CreateTicketMessageFields>({
    resolver: zodResolver(createTicketMessageSchema),
    defaultValues: {
      type: CreateTicketMessageRequestTypeEnum.Reply,
      body: "",
    },
  });

  const messageType = useWatch({ control, name: "type" });
  const isSending = isPending || isSubmitting;

  const onSubmit = (data: CreateTicketMessageFields) => {
    createTicketMessage(
      { ticketId, request: data },
      {
        onSuccess: () => reset(),
        onError: (error) => {
          showErrorToast(createTicketMessageErrorMessage(error));
        },
      },
    );
  };

  const onMessageTypeChange = (type: CreateTicketMessageFields["type"]) => {
    setValue("type", type, {
      shouldDirty: true,
      shouldValidate: true,
    });
  };

  return (
    <form
      className="mt-2 shrink-0 bg-muted/20"
      noValidate
      onSubmit={handleSubmit(onSubmit)}
    >
      <div className="mx-auto w-full max-w-4xl">
        <div className="rounded-lg border border-border bg-background p-2 shadow-xs">
          <input type="hidden" {...register("type")} />
          <Textarea
            aria-label="Antwort verfassen"
            placeholder="Antwort schreiben …"
            aria-invalid={Boolean(errors.body)}
            aria-describedby={errors.body ? "ticket-message-error" : undefined}
            disabled={isSending}
            className="h-10 resize-none border-0 bg-transparent shadow-none focus-visible:ring-0 table-scrollbar"
            {...register("body")}
          />

          {errors.body && (
            <p
              id="ticket-message-error"
              className="mt-2 px-2 pb-2 text-xs text-destructive"
            >
              {errors.body.message}
            </p>
          )}

          {errors.type && (
            <p className="px-2 pb-2 text-xs text-destructive">
              {errors.type.message}
            </p>
          )}

          <TicketComposerActionSelector
            messageType={messageType}
            isSubmitting={isSending}
            onMessageTypeChange={onMessageTypeChange}
          />
        </div>
      </div>
    </form>
  );
};
