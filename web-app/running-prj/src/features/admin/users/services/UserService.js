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
    return axiosClient.put(URL_BACKEND, data)
}
const deleteUserAPI = (id) => {
    const URL_BACKEND = `api/admin/users/${id}`
    return axiosClient.delete(URL_BACKEND)
}
export {
    createUserAPI, getUserWithPaginateAPI, updateUserAPI, deleteUserAPI
}
