import { toast } from "@/components/ui/toast.tsx";

export const showErrorToast = (message: string) => {
  toast.add({
    description: message,
    type: "error",
    timeout: 0,
  });
};
