import React from "react";
import { Nav } from "react-bootstrap";

const FollowTabs = ({ activeTab, onChange, counts }) => {
    const tabs = [
        { key: "discover", label: "Discover", count: counts.discover || 0 },
        { key: "following", label: "Following", count: counts.following || 0 },
        { key: "followers", label: "Followers", count: counts.followers || 0 },
        { key: "friend", label: "Friends", count: counts.friend || 0 },
    ];

    return (
        <Nav
            variant="pills"
            className="follow-tabs flex-nowrap"
            role="tablist"
            aria-label="Follow tabs"
        >
            {tabs.map((tab) => (
                <Nav.Item key={tab.key} className="follow-tabs__item-wrap">
                    <Nav.Link
                    key={tab.key}
                    as="button"
                    type="button"
                    className={`follow-tabs__item ${activeTab === tab.key ? "follow-tabs__item--active" : ""}`}
                    onClick={() => onChange(tab.key)}
                    active={activeTab === tab.key}
                >
                    <span>{tab.label}</span>
                    <span className={`follow-tabs__count ${activeTab === tab.key ? "follow-tabs__count--active" : "follow-tabs__count--inactive"}`}>
                        {tab.count}
                    </span>
                    </Nav.Link>
                </Nav.Item>
            ))}
        </Nav>
    );
};

export default FollowTabs;
