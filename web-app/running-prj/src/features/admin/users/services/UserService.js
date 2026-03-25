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
const getPostsWithPaginateAPI = (currentPage, pageSize, keyword, status) => {
    const URL_BACKEND = `api/admin/posts?page=${currentPage}&size=${pageSize}&keyword=${keyword}&status=${status}`
    return axiosClient.get(URL_BACKEND)
}
const changeApproveStatusPostAPI = (id) => {
    const URL_BACKEND = `api/admin/posts/${id}/approve`
    return axiosClient.put(URL_BACKEND)
}
const changeRejectStatusPostAPI = (id) => {
    const URL_BACKEND = `api/admin/posts/${id}/reject`
    return axiosClient.put(URL_BACKEND)
}
const deletePostAPI = (id) => {
    const URL_BACKEND = `api/admin/posts/${id}`
    return axiosClient.delete(URL_BACKEND)
}
const getFeed = (params) => {
    const URL_BACKEND = `api/user/posts/feed`
    return axiosClient.get(URL_BACKEND, { params })
}
const userCreatePosts = (data) => {
    const URL_BACKEND = `api/user/posts`  
    return axiosClient.post(URL_BACKEND, data)
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

const updateMyProfile = (data) => {
    return axiosClient.put(ApiEndpoints.USERS_API_ENDPOINTS.ME, data);
}

const updateMyAvatar = (imageUrl) => {
    return axiosClient.put(`${ApiEndpoints.USERS_API_ENDPOINTS.ME}/avatar`, {
        imageUrl
    });
}

const search = async (keyword) => {
    return await axiosClient.get(
        `${ApiEndpoints.USERS_API_ENDPOINTS.SEARCH}/${keyword}`
    );
};

const createComment = async (data) => {
    const URL_BACKEND = `api/user/comments`
    return axiosClient.post(URL_BACKEND, data)
}
const getCommentsByPostId = async (postId) => {
    const URL_BACKEND = `api/user/comments/${postId}`
    return axiosClient.get(URL_BACKEND)
}
const replyComment = async (data) => {
    const URL_BACKEND = `api/user/comments`
    return axiosClient.post(URL_BACKEND, data)
}
const countReplyPost = async (postId) => {
    const URL_BACKEND = `api/user/posts/${postId}/count`
    return axiosClient.get(URL_BACKEND)
}
const reactPost = async (postId, reactionType) => {
    const URL_BACKEND = `api/user/posts/${postId}/react?type=${reactionType}`
    return axiosClient.post(URL_BACKEND, { reactionType })
}
const getPostReactions = async (postId) => {
    const URL_BACKEND = `api/user/posts/${postId}/reactions`
    return axiosClient.get(URL_BACKEND)
}
const reactComment = async (commentId, reactionType) => {
    const URL_BACKEND = `api/user/posts/comments/${commentId}/react?type=${reactionType}`
    return axiosClient.post(URL_BACKEND, { reactionType })
}
const getCommentReaction = async (commentId) => {
    const URL_BACKEND = `api/user/posts/comments/${commentId}/reactions`
    return axiosClient.get(URL_BACKEND)
}
const countCommentReply = async (commentId) => {
    const URL_BACKEND = `api/user/comments/${commentId}/count`
    return axiosClient.get(URL_BACKEND)
}
export {
    createUserAPI,
    getUserWithPaginateAPI,
    updateUserAPI,
    deleteUserAPI,
    getUserInfo,
    updateMyProfile,
    updateMyAvatar,
    search,
    getPostsWithPaginateAPI,
    changeApproveStatusPostAPI,
    changeRejectStatusPostAPI,
    deletePostAPI,
    getFeed, 
    userCreatePosts,
    createComment,
    getCommentsByPostId,
    replyComment,
    countReplyPost,
    reactPost,
    getPostReactions,
    reactComment,
    getCommentReaction,
    countCommentReply,
}