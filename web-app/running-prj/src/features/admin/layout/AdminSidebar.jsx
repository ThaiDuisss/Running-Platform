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
    Newspaper
} from "react-bootstrap-icons";
import "@/style/AdminSidebar.css"
const AdminSidebar = ({ collapsed, setCollapsed }) => {

    const navigate = useNavigate();
    const location = useLocation();
    const { logout } = useContext(AuthActionContext);

    const width = collapsed ? 70 : 220;

    return (
        <div
            style={{
                width: width,
                height: "100vh",
                position: "fixed",
                background: "#f8f9fa",
                borderRight: "1px solid #ddd",
                transition: "all 0.3s"
            }}
            className="d-flex flex-column"
        >

            {/* Toggle button */}
            <div className="p-3 border-bottom">

                <button
                    className="btn btn-light w-100"
                    onClick={() => setCollapsed(!collapsed)}
                >
                    <List />
                </button>

            </div>

            {/* Logo */}

            <Nav className="flex-column px-2">

                <Nav.Link
                    active={location.pathname.includes("dashboard")}
                    onClick={() => navigate("/admin/dashboard")}
                    className="d-flex align-items-center gap-2"
                >
                    <House />
                    {!collapsed && "Dashboard"}
                </Nav.Link>

                <Nav.Link
                    active={location.pathname.includes("user")}
                    onClick={() => navigate("/admin/user")}
                    className="d-flex align-items-center gap-2"
                >
                    <People />
                    {!collapsed && "Users"}
                </Nav.Link>

                <Nav.Link
                    active={location.pathname.includes("posts")}
                    onClick={() => navigate("/admin/posts")}
                    className="d-flex align-items-center gap-2"
                >
                    <FileText />
                    {!collapsed && "Posts"}
                </Nav.Link>
                 <Nav.Link
                    active={location.pathname.includes("articles")}
                    onClick={() => navigate("/admin/articles")}
                    className="d-flex align-items-center gap-2"
                >
                    <Newspaper />
                    {!collapsed && "Articles"}
                </Nav.Link>

            </Nav>

            {/* Logout
            <Nav className="mt-auto px-2 mb-3">

                <Nav.Link
                    className="text-danger d-flex align-items-center gap-2"
                    onClick={() => {
                        logout();
                        navigate("/");
                    }}
                >
                    <BoxArrowRight />
                    {!collapsed && "Logout"}
                </Nav.Link>

            </Nav> */}

        </div>
    );
};

export default AdminSidebar;