import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { useGetServices } from "@/features/services/hooks/useGetServices.ts";

type SelectServiceProps = {
  value?: number;
  onValueChange: (serviceTeamId: number | null) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SelectService = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: SelectServiceProps) => {
  const { services = [], isLoading, isError } = useGetServices();

  const isDisabled = isLoading || isError || services.length === 0;

  return (
    <Select
      value={value ?? null}
      onValueChange={onValueChange}
      items={services.map((service) => ({
        value: service.id,
        label: service.name,
      }))}
      disabled={isDisabled}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Service auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {services.map((service) => (
          <SelectItem key={service.id} value={service.id}>
            {service.name}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
