import AdminDashboard from "@/features/admin/dashboard/pages/AdminDashboard";
import AdminLayout from "../layouts/AdminLayout"; "../layouts/AdminLayout";
import UserPage from "@/features/admin/users/pages/UserPage";
import PostPage from "@/features/admin/posts/PostPage";
import ArticlePage from "@/features/admin/article/pages/ArticlePage";

const adminRoutes = [
    {
        path: "/admin",
        element: <AdminLayout />,
        children: [
            { path: "user", element: <UserPage /> },
            { path: "posts", element: <PostPage /> },
            { path: "articles", element: <ArticlePage /> },
        ]
    }
]
export default adminRoutes