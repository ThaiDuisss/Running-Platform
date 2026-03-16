import uploadFile from '../../../../shared/services/UploadService';
import axiosClient from "@/shared/services/axiosClient"

const handleCreateArticle = async (data) => {
    console.log("data before create:", data);

    if (!data.image) {
        throw new Error("Upload thumbnail before creating article");
    }

    if (!data.category) {
        throw new Error("Choose category");
    }

    try {
        // 1. Upload ảnh trước
        const thumbnailUrl = await uploadFile(data.image, "images/articles");

        // 2. Tạo request body đúng với ArticleRequest
        const formData = {
            title: data.title,
            summary: data.summary,
            content: data.content,
            thumbnailUrl: thumbnailUrl,
            category: data.category
        };

        // 3. Gửi request
        const response = await axiosClient.post("/api/articles", formData);

        return response;

    } catch (error) {

        const allErrors = error.response?.data?.data
            ?.map(e => e.message)
            .join(", ");

        console.error("All error:", allErrors);

        throw new Error(allErrors || "Create article failed");
    }
};

const getArticleWithPaginateAPI = async (page, size) => {
    try {
        const response = await axiosClient.get("/api/articles", {
            params: {
                page,
                size
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching articles:", error);
        throw new Error("Failed to fetch articles");
    }
}

const updateArticleAPI = async (articleId, data) => {
    console.log("data before update:", data);
    try {
        let thumbnailUrl = data.thumbnailUrl;
        // Nếu có upload ảnh mới thì upload trước
        if (data.image) {
            thumbnailUrl = await uploadFile(data.image, "images/articles");
        }
        // Body gửi lên backend
        const formData = {
            title: data.title,
            summary: data.summary,
            content: data.content,
            thumbnailUrl: thumbnailUrl,
            category: data.category
        };
        const response = await axiosClient.put(
            `/api/articles/${articleId}`,
            formData
        );
        return response;
    } catch (error) {
        const allErrors = error.response?.data?.data
            ?.map(e => e.message)
            .join(", ");
        console.error("All error:", allErrors);
        throw new Error(allErrors || "Update article failed");
    }
};

const deleteArticleAPI = async (articleId) => {
    try {
        const response = await axiosClient.delete(`/api/articles/${articleId}`);
        return response;
    } catch (error) {
        const message = error.response?.data?.message;
        console.error("Delete article error:", message);
        throw new Error(message || "Delete article failed");
    }
};

export {
    handleCreateArticle,
    getArticleWithPaginateAPI,
    updateArticleAPI,
    deleteArticleAPI
}
