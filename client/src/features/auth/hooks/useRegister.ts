import {useMutation} from "@tanstack/react-query";
import {register} from "@/features/auth/api/auth.ts";

export const useRegister = () => {
    return useMutation({
        mutationFn: register
    })
}