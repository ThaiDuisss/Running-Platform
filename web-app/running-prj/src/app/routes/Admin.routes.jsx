import AdminDashboard from "@/features/admin/dashboard/pages/AdminDashboard";
import AdminLayout from "../layouts/AdminLayout";
import UserPage from "@/features/admin/users/pages/UserPage";
import PostPage from "@/features/admin/posts/PostPage";
import ArticlePage from "@/features/admin/article/pages/ArticlePage";
import RouteChallengePage from "@/features/admin/route-challenges/pages/RouteChallengePage";
import { Navigate } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import HighlightRoutePage from "@/features/admin/HighlightRoute/HighlightRoutePage";

const adminRoutes = [
    {
        path: "/admin",
        element:
            <ProtectedRoute roles={["ADMIN"]}>
                <AdminLayout />
            </ProtectedRoute>
        ,
        children: [
            { index: true, element: <Navigate to="dashboard" replace /> },
            { path: "dashboard", element: <AdminDashboard /> },
            { path: "user", element: <UserPage /> },
            { path: "posts", element: <PostPage /> },
            { path: "articles", element: <ArticlePage /> },
            { path: "route-challenges", element: <RouteChallengePage /> },
            { path: "highlight-routes", element: <HighlightRoutePage /> }
        ]
    }
]
export default adminRoutes