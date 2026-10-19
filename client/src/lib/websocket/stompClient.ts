import { Client, type StompSubscription } from "@stomp/stompjs";

const apiUrl = import.meta.env.VITE_API_URL;

type MessageHandler = (body: string) => void;

const listeners = new Map<string, Set<MessageHandler>>();
const subscriptions = new Map<string, StompSubscription>();

export const stompClient = new Client({
  brokerURL: `${apiUrl.replace(/^http/, "ws")}/ws`,

  reconnectDelay: 5000,

  onConnect: () => {
    subscriptions.clear();

    for (const destination of listeners.keys()) {
      subscribeToBroker(destination);
    }
  },

  onStompError: (frame) => {
    console.error("STOMP error:", frame.headers.message);
  },
});

const subscribeToBroker = (destination: string) => {
  if (!stompClient.connected || subscriptions.has(destination)) {
    return;
  }

  const subscription = stompClient.subscribe(destination, (message) => {
    const handlers = listeners.get(destination);

    handlers?.forEach((handler) => handler(message.body));
  });

  subscriptions.set(destination, subscription);
};

export const subscribe = (destination: string, handler: MessageHandler) => {
  let handlers = listeners.get(destination);

  if (!handlers) {
    handlers = new Set();
    listeners.set(destination, handlers);
  }

  handlers.add(handler);

  subscribeToBroker(destination);

  return () => {
    const currentHandlers = listeners.get(destination);

    currentHandlers?.delete(handler);

    if (currentHandlers?.size === 0) {
      listeners.delete(destination);

      subscriptions.get(destination)?.unsubscribe();
      subscriptions.delete(destination);
    }
  };
};
