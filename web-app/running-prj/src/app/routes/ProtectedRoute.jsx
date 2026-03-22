import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthDataContext } from "../providers/AuthProvider";

const ProtectedRoute = ({ children, roles = [] }) => {
    const { user } = useContext(AuthDataContext);
    const storedUser = localStorage.getItem("userInfo");
    const currentUser = user || (storedUser ? JSON.parse(storedUser) : null);
    console.log("currentUser", currentUser)
    if (!currentUser) {
        return <Navigate to="/login" replace />;
    }

    if (roles.length > 0) {
        const userRoles = (currentUser.roles || []).map((item) => item.roleName || item.name);

        const hasPermission = roles.some((role) =>
            userRoles.includes(role)
        );

        if (!hasPermission) {
            return <Navigate to="/unauthorized" replace />;
        }
    }

    return children;
};

export default ProtectedRoute;
