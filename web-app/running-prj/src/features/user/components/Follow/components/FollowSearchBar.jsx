import React from "react";
import { Form, InputGroup } from "react-bootstrap";
import { Search } from "lucide-react";

const FollowSearchBar = ({ value, onChange }) => (
    <InputGroup className="follow-search">
        <InputGroup.Text className="follow-search__icon">
            <Search size={18} />
        </InputGroup.Text>
        <Form.Control
            value={value}
            onChange={(event) => onChange(event.target.value)}
            placeholder="Tìm theo tên hoặc số điện thoại"
            className="follow-search__input"
        />
    </InputGroup>
);

export default FollowSearchBar;
