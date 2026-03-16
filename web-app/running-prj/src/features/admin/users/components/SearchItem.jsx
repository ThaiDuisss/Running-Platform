import { FaSearch } from "react-icons/fa";
import "./SearchItem.css";

const SearchItem = ({ search, onSearch }) => {

    return (
        <div className="search-container">
            <input
                type="text"
                className="search-input"
                placeholder="Search..."
                value={search}
                onChange={(e) => onSearch(e.target.value)}
            />

            <FaSearch className="search-icon" />
        </div>
    );
};

export default SearchItem;