import axios from "axios";

const uploadFile = async (file, uploadFolder) => {
    try {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("uploadFolder", uploadFolder)
        const token = localStorage.getItem("access_token");
        const response = await axios.post(`/api/upload-file`, formData, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
            },
            baseURL: "http://localhost:8080",
        });
        return response.data.data
    } catch (error) {
        throw new Error(error || "can not upload file");
    }
};

export default uploadFile