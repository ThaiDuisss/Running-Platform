import { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthDataContext } from "../providers/AuthProvider";

const ProtectedRoute = ({ children, roles = [] }) => {
    const isEmptyObject = (obj) =>
        obj && Object.keys(obj).length === 0;
    const { user } = useContext(AuthDataContext);
    const [loading, setLoading] = useState(true);
    const storedUser = localStorage.getItem("userInfo");
    const currentUser = user || (storedUser ? JSON.parse(storedUser) : null);
    console.log("currentUser", currentUser)

    useEffect(() => {
        setLoading(false);
    }, [])
    if (!loading && (!currentUser || isEmptyObject(currentUser))) {
        console.log("an vao day", currentUser)
        return <Navigate to="/login" replace />;
    }



    if (roles.length > 0 && !loading) {
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
