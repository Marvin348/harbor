import { useState } from "react";
import { CreateServiceDialog } from "@/features/services/components/CreateServiceDialog";
import { ServiceCatalog } from "@/features/services/components/ServiceCatalog";
import { ServiceHeader } from "@/features/services/components/ServiceHeader";

export const ServicesPage = () => {
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);

  return (
    <div>
      <ServiceHeader onCreateService={() => setIsCreateDialogOpen(true)} />

      <ServiceCatalog onCreateService={() => setIsCreateDialogOpen(true)} />

      <CreateServiceDialog
        open={isCreateDialogOpen}
        onOpenChange={setIsCreateDialogOpen}
      />
    </div>
  );
};
