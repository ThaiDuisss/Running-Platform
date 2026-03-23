import React from "react";
import { Form } from "react-bootstrap";
import { Search } from "lucide-react";

const FollowSearchBar = ({ value, onChange }) => (
    <label className="follow-search">
        <Search size={18} className="follow-search__icon" />
        <Form.Control
            value={value}
            onChange={(event) => onChange(event.target.value)}
            placeholder="Tìm theo tên hoặc số điện thoại"
            className="follow-search__input"
        />
    </label>
);

export default FollowSearchBar;
