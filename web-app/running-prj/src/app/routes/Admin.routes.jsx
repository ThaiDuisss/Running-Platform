import AdminDashboard from "@/features/admin/dashboard/pages/AdminDashboard";
import AdminLayout from "../layouts/AdminLayout"; "../layouts/AdminLayout";
import UserPage from "@/features/admin/users/pages/UserPage";
import PostPage from "@/features/admin/posts/PostPage";

const adminRoutes = [
    {
        path: "/admin",
        element: <AdminLayout />,
        children: [
            { path: "user", element: <UserPage /> },
            { path: "posts", element: <PostPage /> },

        ]
    }
]
export default adminRoutes