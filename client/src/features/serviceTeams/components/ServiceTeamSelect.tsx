import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useGetServiceTeamOptions } from "@/features/serviceTeams/hooks/useGetServiceTeamOptions.ts";

type ServiceTeamSelectProps = {
  value?: number;
  onValueChange: (serviceTeamId: number | null) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const ServiceTeamSelect = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: ServiceTeamSelectProps) => {
  const {
    serviceTeamOptions = [],
    isLoading,
    isError,
  } = useGetServiceTeamOptions();

  const isDisabled = isLoading || isError || serviceTeamOptions.length === 0;

  return (
    <Select
      value={value ?? null}
      onValueChange={(serviceTeamId) => {
        if (value !== null) {
          onValueChange(serviceTeamId);
        }
      }}
      items={serviceTeamOptions.map((serviceTeam) => ({
        value: serviceTeam.id,
        label: serviceTeam.name,
      }))}

      disabled={isDisabled}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Service-Team auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {serviceTeamOptions.map((serviceTeam) => (
          <SelectItem key={serviceTeam.id} value={serviceTeam.id}>
            {serviceTeam.name}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
