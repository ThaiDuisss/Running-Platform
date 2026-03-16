import {
    io
} from "socket.io-client";


let socket = null;

export function connectSocket(variable, port) {
    let socketUrl;

    if (variable) {
        // Nếu variable có giá trị -> gửi email
        socketUrl = `http://localhost:${port}?email=${variable}`;
    } else {
        // Nếu variable null/undefined -> gửi token
        const token = localStorage.getItem("access_token");
        socketUrl = `http://localhost:${port}?token=${token}`;
    }

    socket = io(socketUrl);

    socket.on("connect", () => {
        console.log("✅ Connected to socket.io server:", socket.id);
    });

    socket.on("disconnect", () => {
        console.log("❌ Disconnected from socket.io server");
    });

    return socket;
}

export function getSocket() {
    return socket;
}