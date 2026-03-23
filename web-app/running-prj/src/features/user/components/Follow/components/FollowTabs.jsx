import React from "react";

const FollowTabs = ({ activeTab, onChange, counts }) => {
    const tabs = [
        { key: "discover", label: "Discover", count: counts.discover || 0 },
        { key: "following", label: "Following", count: counts.following || 0 },
        { key: "followers", label: "Followers", count: counts.followers || 0 },
    ];

    return (
        <div className="follow-tabs" role="tablist" aria-label="Follow tabs">
            {tabs.map((tab) => (
                <button
                    key={tab.key}
                    type="button"
                    className={`follow-tabs__item ${activeTab === tab.key ? "follow-tabs__item--active" : ""}`}
                    onClick={() => onChange(tab.key)}
                >
                    <span>{tab.label}</span>
                    <span className="follow-tabs__count">{tab.count}</span>
                </button>
            ))}
        </div>
    );
};

export default FollowTabs;
