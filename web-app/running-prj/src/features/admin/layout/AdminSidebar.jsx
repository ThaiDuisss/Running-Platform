
import { Nav } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { useContext } from "react";
import { AuthActionContext } from "@/app/providers/AuthProvider";
import {
    House,
    People,
    BoxArrowRight,
    List,
    FileText,
    Newspaper,
    Trophy
} from "react-bootstrap-icons";
import "@/style/AdminSidebar.css";

const AdminSidebar = ({ collapsed, setCollapsed }) => {

    const navigate = useNavigate();
    const location = useLocation();
    const { logout } = useContext(AuthActionContext);

    const width = collapsed ? 70 : 250;

    const menuItems = [
        {
            label: "Dashboard",
            icon: <House />,
            path: "/admin/dashboard",
            active: location.pathname.includes("dashboard")
        },
        {
            label: "Users",
            icon: <People />,
            path: "/admin/user",
            active: location.pathname.includes("user")
        },
        {
            label: "Posts",
            icon: <FileText />,
            path: "/admin/posts",
            active: location.pathname.includes("posts")
        },
        {
            label: "Route Challenges",
            icon: <Trophy />,
            path: "/admin/route-challenges",
            active: location.pathname.includes("route-challenges")
        },
        {
            label: "Highlight Routes",
            icon: <Newspaper />,
            path: "/admin/highlight-routes",
            active: location.pathname.includes("highlight-routes")
        }
    ];

    return (
        <div
            className={`admin-sidebar ${collapsed ? "collapsed" : ""}`}
            style={{ width }}
        >

            {/* Toggle button */}
            <div className="p-3 border-bottom">
                <button
                    className="toggle-btn w-100 p-2"
                    onClick={() => setCollapsed(!collapsed)}
                >
                    <List />
                </button>
            </div>

            {/* Menu */}
            <Nav className="flex-column px-2 mt-2">

                {menuItems.map((item, index) => (
                    <Nav.Link
                        key={index}
                        active={item.active}
                        onClick={() => navigate(item.path)}
                        className="d-flex align-items-center gap-2"
                    >
                        {item.icon}
                        {!collapsed && item.label}
                    </Nav.Link>
                ))}
                <Nav.Link
                    active={location.pathname.includes("articles")}
                    onClick={() => navigate("/admin/articles")}
                    className="d-flex align-items-center gap-2"
                >
                    <Newspaper />
                    {!collapsed && "Articles"}
                </Nav.Link>

            </Nav>

            {/* Logout */}
            <Nav className="logout px-2 mb-3">
                <Nav.Link
                    className="d-flex align-items-center gap-2"
                    onClick={() => {
                        logout();
                        navigate("/");
                    }}
                >
                    <BoxArrowRight />
                    {!collapsed && "Logout"}
                </Nav.Link>
            </Nav>

        </div>
    );
};

export default AdminSidebar;
