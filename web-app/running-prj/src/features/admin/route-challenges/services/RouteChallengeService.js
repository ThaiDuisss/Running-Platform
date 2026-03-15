import axiosClient from "@/shared/services/axiosClient";

const URL_BASE = "api/admin/route-challenges";

const createRouteChallengeAPI = (data) => {
    const URL_BACKEND = URL_BASE;
    return axiosClient.post(URL_BACKEND, data);
};

const getRouteChallengesWithPaginateAPI = (currentPage, pageSize, keyword) => {
    const URL_BACKEND = URL_BASE + `?page=${currentPage}&size=${pageSize}`;
    return axiosClient.get(URL_BACKEND);
};

const getRouteChallengeByIdAPI = (id) => {
    const URL_BACKEND = URL_BASE + `/${id}`;
    return axiosClient.get(URL_BACKEND);
};

const updateRouteChallengeAPI = (id, data) => {
    const URL_BACKEND = URL_BASE + `/${id}`;
    return axiosClient.put(URL_BACKEND, data);
};

const deleteRouteChallengeAPI = (id) => {
    const URL_BACKEND = URL_BASE + `/${id}`;
    return axiosClient.delete(URL_BACKEND);
};

export {
    createRouteChallengeAPI,
    getRouteChallengesWithPaginateAPI,
    getRouteChallengeByIdAPI,
    updateRouteChallengeAPI,
    deleteRouteChallengeAPI,
};