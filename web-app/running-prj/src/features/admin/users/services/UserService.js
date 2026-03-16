import App from "@/app/App"
import {
    ApiEndpoints
} from "@/app/services/AppUrlConstant"
import axiosClient from "@/shared/services/axiosClient"

const createUserAPI = (data) => {
    const URL_BACKEND = "api/admin/users"
    return axiosClient.post(URL_BACKEND, data)
}
const getUserWithPaginateAPI = (currentPage, pageSize, keyword) => {
    const URL_BACKEND = `api/admin/users?page=${currentPage}&size=${pageSize}&keyword=${keyword}`
    return axiosClient.get(URL_BACKEND)
}
const updateUserAPI = (data, id) => {
    const URL_BACKEND = `api/admin/users/${id}`
    return axiosClient.put(URL_BACKEND, data, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}
const deleteUserAPI = (id) => {
    const URL_BACKEND = `api/admin/users/${id}`
    return axiosClient.delete(URL_BACKEND)

}
const getUserInfo = () => {
    return axiosClient.get(ApiEndpoints.USERS_API_ENDPOINTS.ME, {
        withCredentials: true
    })
}

const search = async (keyword) => {
    return await axiosClient.get(
        `${ApiEndpoints.USERS_API_ENDPOINTS.SEARCH}/${keyword}`
    );
};
export {
    createUserAPI,
    getUserWithPaginateAPI,
    updateUserAPI,
    deleteUserAPI,
    getUserInfo,
    search
}