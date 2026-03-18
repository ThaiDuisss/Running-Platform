import axiosClient from "@/shared/services/axiosClient";

const BASE = "/api/friends";

export const friendService = {
    getFriends: (page = 0, size = 10) =>
        axiosClient.get(`${BASE}?page=${page}&size=${size}`),

    getSentRequests: (page = 0, size = 10) =>
        axiosClient.get(`${BASE}/requests/sent?page=${page}&size=${size}`),

    getReceivedRequests: (page = 0, size = 10) =>
        axiosClient.get(`${BASE}/requests/received?page=${page}&size=${size}`),

    sendRequest: (userId) => axiosClient.post(`${BASE}/request/${userId}`),

    cancelRequest: (userId) => axiosClient.delete(`${BASE}/request/${userId}`),

    acceptRequest: (requestId) => axiosClient.put(`${BASE}/request/${requestId}/accept`),

    rejectRequest: (requestId) => axiosClient.put(`${BASE}/request/${requestId}/reject`),

    unfriend: (userId) => axiosClient.delete(`${BASE}/unfriend/${userId}`),
};