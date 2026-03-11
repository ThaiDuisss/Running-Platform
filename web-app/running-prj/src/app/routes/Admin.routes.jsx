import AdminDashboard from "@/features/admin/dashboard/pages/AdminDashboard";
import AdminLayout from "../layouts/AdminLayout"; "../layouts/AdminLayout";
import UserPage from "@/features/admin/users/pages/UserPage"; "@/features/admin/users/pages/UserPage";

const adminRoutes = [
    {
        path: "/admin",
        element: <AdminLayout />,
        children: [
            { path: "user", element: <UserPage /> },
            { index: true, path: "dashboard", element: <AdminDashboard /> },

        ]
    }
]
export default adminRoutes