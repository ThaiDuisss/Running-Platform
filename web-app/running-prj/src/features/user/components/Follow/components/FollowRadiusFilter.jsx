import React from "react";
import { Button, ButtonGroup } from "react-bootstrap";

const FollowRadiusFilter = ({ value, onChange, options }) => (
    <div className="follow-filter">
        <span className="follow-filter__label">Bán kính</span>
        <ButtonGroup className="follow-filter__chips flex-wrap">
            {options.map((option) => (
                <Button
                    key={option.value ?? "all"}
                    variant={value === option.value ? "dark" : "outline-secondary"}
                    className={`follow-chip ${value === option.value ? "follow-chip--active" : ""}`}
                    onClick={() => onChange(option.value)}
                >
                    {option.label}
                </Button>
            ))}
        </ButtonGroup>
    </div>
);

export default FollowRadiusFilter;
