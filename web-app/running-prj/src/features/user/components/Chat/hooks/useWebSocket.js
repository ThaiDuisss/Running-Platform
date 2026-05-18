// src/features/user/components/Chat/hooks/useWebSocket.js
import {
    useEffect,
    useRef,
    useCallback,
    useState
} from 'react';
import SockJS from 'sockjs-client';
import {
    Client
} from '@stomp/stompjs';

const WS_URL =
    import.meta.env.VITE_API_URL || 'http://localhost:8080';

export function useWebSocket() {
    const clientRef = useRef(null);
    const [connected, setConnected] = useState(false);
    // Store pending subscriptions that came in before connection was established
    const pendingSubsRef = useRef([]);

    useEffect(() => {
        const token = localStorage.getItem('access_token'); // match  token 
        if (!token) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${WS_URL}/ws`), 
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            reconnectDelay: 4000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                setConnected(true);
                // Flush pending subscriptions
                pendingSubsRef.current.forEach(({
                    destination,
                    callback,
                    resolve
                }) => {
                    const sub = client.subscribe(destination, (msg) => callback(JSON.parse(msg.body)));
                    resolve(sub);
                });
                pendingSubsRef.current = [];
            },
            onDisconnect: () => setConnected(false),
            onStompError: (frame) => console.error('[WS] STOMP error', frame),
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
            clientRef.current = null;
        };
    }, []);

    const subscribe = useCallback((destination, callback) => {
        return new Promise((resolve) => {
            if (clientRef.current.connected) {
                const sub = clientRef.current.subscribe(destination, (msg) =>
                    callback(JSON.parse(msg.body))
                );
                resolve(sub);
            } else {
                // Queue for when connection is ready
                pendingSubsRef.current.push({
                    destination,
                    callback,
                    resolve
                });
            }
        });
    }, []);

    const publish = useCallback((destination, body) => {
        if (!clientRef.current.connected) {
            console.warn('[WS] Not connected, cannot publish');
            return;
        }
        clientRef.current.publish({
            destination,
            body: JSON.stringify(body),
        });
    }, []);

    return {
        connected,
        subscribe,
        publish
    };
}