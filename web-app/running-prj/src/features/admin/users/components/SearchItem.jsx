import { FaSearch } from "react-icons/fa";
import "./SearchItem.css";

const SearchItem = ({ search, onSearch }) => {

    return (
        <div className="search-container">
            <input
                type="text"
                className="search-input"
                placeholder="Search user..."
            />

            <i className="bi bi-search search-icon"></i>
        </div>
    );
};

export default SearchItem;