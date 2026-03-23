import React from "react";
import { Link } from "react-router-dom";

export default function SidebarLeft() {

    return (
        <div className="sidebar-left">

            <div className="profile">
                <img src="https://i.pravatar.cc/40" />
                <span>Do Xuan Thai</span>
            </div>

            <div className="menu-item">🏃‍♂️ Running Feed</div>
            <Link className="menu-item" to="/follow">👥 Following</Link>
            <div className="menu-item">📊 Activities</div>

        </div>
    )
}
