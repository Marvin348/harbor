import { stompClient } from "@/lib/websocket/stompClient.ts";
import { useEffect } from "react";

export function useWebSocket() {
  useEffect(() => {
    stompClient.activate();

    return () => {
      void stompClient.deactivate();
    };
  }, []);
}
