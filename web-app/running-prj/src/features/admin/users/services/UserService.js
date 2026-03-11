import axiosClient from "@/shared/services/axiosClient"

const createUserAPI = (data) => {
    const URL_BACKEND = "api/admin/user"
    return axiosClient.post(URL_BACKEND,data)
}
export {
    createUserAPI
}