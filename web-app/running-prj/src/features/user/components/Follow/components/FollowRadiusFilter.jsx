import React from "react";

const FollowRadiusFilter = ({ value, onChange, options }) => (
    <div className="follow-filter">
        <span className="follow-filter__label">Bán kính</span>
        <div className="follow-filter__chips">
            {options.map((option) => (
                <button
                    key={option.value ?? "all"}
                    type="button"
                    className={`follow-chip ${value === option.value ? "follow-chip--active" : ""}`}
                    onClick={() => onChange(option.value)}
                >
                    {option.label}
                </button>
            ))}
        </div>
    </div>
);

export default FollowRadiusFilter;
