import { toast } from "@/components/ui/toast.tsx";

export const showSuccessToast = (message: string) => {
  toast.add({
    description: message,
    type: "success",
    timeout: 4000,
  });
};
