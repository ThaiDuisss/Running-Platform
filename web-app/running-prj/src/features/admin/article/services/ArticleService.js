import { uploadFile } from '../../../../shared/services/UploadService';
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

export {
    handleCreateArticle
}
