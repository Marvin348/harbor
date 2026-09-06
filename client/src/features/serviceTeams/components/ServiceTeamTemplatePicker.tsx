import { SERVICE_TEAM_TEMPLATES } from "@/features/serviceTeams/constants/serviceTeamTemplates.ts";
import { Button } from "@/components/ui/button.tsx";
import { Sparkles } from "lucide-react";

export const ServiceTeamTemplatePicker = () => {
  return (
    <div className="grid gap-2">
      <div className="flex items-center justify-between gap-3">
        <p className="text-sm font-medium">Vorlage wählen</p>
        <Button
          type="button"
          variant="outline"
          size="icon-sm"
          aria-label="AI-Vorschlag"
          title="AI-Vorschlag"
        >
          <Sparkles />
        </Button>
      </div>
      <div className="grid gap-2">
        {SERVICE_TEAM_TEMPLATES.map((template) => {
          const Icon = template.icon;
          // const isSelected = selectedTemplate.name === template.name;

          return (
            <button
              key={template.name}
              type="button"
              // onClick={() => chooseTemplate(template)}
              className="flex items-center gap-3 rounded-md border px-3 py-2 text-left transition-colors"
            >
              <div className="flex size-8 rounded-md shrink-0 items-center justify-center border">
                <Icon className="size-4" />
              </div>

              <div className="flex flex-col gap-1">
                <span className="min-w-0 flex-1 truncate text-sm font-medium">
                  {template.name}
                </span>
                <span className="text-xs text-muted-foreground">
                  {template.description}
                </span>
              </div>

              {/*{isSelected ? <Check className="size-4" /> : null}*/}
            </button>
          );
        })}
      </div>
    </div>
  );
};
