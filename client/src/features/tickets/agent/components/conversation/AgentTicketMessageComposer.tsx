import { Paperclip, Send, Sparkles } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";

export const AgentTicketMessageComposer = () => {
  return (
    <div className="mt-6 bg-muted/20 ">
      <div className="mx-auto w-full max-w-4xl">
        <div className="rounded-lg border border-border bg-background p-2 shadow-xs">
          <Textarea
            aria-label="Antwort verfassen"
            placeholder="Antwort schreiben …"
            className="min-h-20 resize-none border-0 bg-transparent shadow-none focus-visible:ring-0"
          />

          <div className="flex items-center justify-between border-t border-border px-1 pt-2">
            <div className="flex items-center">
              <Button
                type="button"
                variant="ghost"
                size="icon-sm"
                aria-label="Datei anhängen"
              >
                <Sparkles />
              </Button>
              <Button
                type="button"
                variant="ghost"
                size="icon-sm"
                aria-label="Datei anhängen"
              >
                <Paperclip />
              </Button>
            </div>

            <Button type="button">
              <Send />
              Senden
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
