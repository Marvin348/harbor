import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useGetServiceTeams } from "@/features/serviceTeams/hooks/useGetServiceTeams";

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
  const { serviceTeams = [], isLoading, isError } = useGetServiceTeams();

  const isDisabled = isLoading || isError || serviceTeams.length === 0;

  return (
    <Select
      value={value ?? null}
      onValueChange={(serviceTeamId) => {
        if (value !== null) {
          onValueChange(serviceTeamId);
        }
      }}
      items={serviceTeams.map((serviceTeam) => ({
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
        {serviceTeams.map((serviceTeam) => (
          <SelectItem key={serviceTeam.id} value={serviceTeam.id}>
            {serviceTeam.name}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
