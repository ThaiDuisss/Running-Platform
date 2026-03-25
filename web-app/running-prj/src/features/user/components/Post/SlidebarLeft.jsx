import { AuthDataContext } from "@/app/providers/AuthProvider";
import { getUserInfo } from "@/features/admin/users/services/UserService";
import React, { useContext } from "react";
import { Link } from "react-router-dom";

export default function SidebarLeft() {
    const { user } = useContext(AuthDataContext);

    React.useEffect(() => {
    }, []);

    return (
        <div className="sidebar-left">
            <div className="profile">
                <img src={user?.imageUrl} alt="avatar" style={{ borderRadius: "50%", width: 36, height: 36 }} />
                <span>{user?.username}</span>
            </div>

            <Link to="/feed" className="menu-item">
                <span style={{ fontSize: "20px" }}>📰</span> Bảng tin (Feed)
            </Link>
            <Link className="menu-item" to="/follow">
                <span style={{ fontSize: "20px" }}>👥</span> Đang theo dõi
            </Link>

        </div>
    );
}