import React, { useState, useEffect, useCallback } from "react";
import PropTypes from "prop-types";
import {
  Modal,
  Form,
  ListGroup,
  Spinner,
  Alert,
  Image,
  InputGroup,
  Button
} from "react-bootstrap";
import { Search, X } from "react-bootstrap-icons";

import { search as searchUsers } from "@/features/admin/users/services/UserService";

const NewChatPopover = ({ open, onClose, onSelectUser }) => {

  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [hasSearched, setHasSearched] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = useCallback(async (query) => {

    if (!query?.trim()) {
      setSearchResults([]);
      setHasSearched(false);
      return;
    }

    setLoading(true);
    setHasSearched(true);
    setError(null);

    try {

      const response = await searchUsers(query.trim());

      if (response?.data) {
        setSearchResults(response.data);
      } else {
        setSearchResults([]);
      }

    } catch (err) {

      console.error("Error searching users:", err);
      setError("Failed to search users. Please try again.");
      setSearchResults([]);

    } finally {
      setLoading(false);
    }

  }, []);

  useEffect(() => {

    const timeoutId = setTimeout(() => {

      if (searchQuery) {
        handleSearch(searchQuery);
      } else {
        setSearchResults([]);
        setHasSearched(false);
        setError(null);
      }

    }, 500);

    return () => clearTimeout(timeoutId);

  }, [searchQuery, handleSearch]);

  const handleClearSearch = () => {

    setSearchQuery("");
    setSearchResults([]);
    setHasSearched(false);
    setError(null);

  };

  const handleUserSelect = (user) => {

    onSelectUser(user);

    setSearchQuery("");
    setSearchResults([]);
    setHasSearched(false);

    onClose();

  };

  return (

    <Modal show={open} onHide={onClose} centered>

      <Modal.Header closeButton>
        <Modal.Title>
          Start a new conversation
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>

        {/* SEARCH INPUT */}

        <InputGroup className="mb-3">

          <InputGroup.Text>
            <Search />
          </InputGroup.Text>

          <Form.Control
            placeholder="Search users..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            autoFocus
          />

          {searchQuery && (
            <Button
              variant="outline-secondary"
              onClick={handleClearSearch}
            >
              <X />
            </Button>
          )}

        </InputGroup>

        {/* RESULT AREA */}

        <div style={{ maxHeight: 300, overflowY: "auto" }}>

          {loading && (
            <div className="text-center p-3">
              <Spinner animation="border" />
            </div>
          )}

          {!loading && error && (
            <Alert variant="danger">
              {error}
            </Alert>
          )}

          {!loading && !error && searchResults.length > 0 && (

            <ListGroup>

              {searchResults.map((user) => (

                <ListGroup.Item
                  key={user.userId}
                  action
                  onClick={() => handleUserSelect(user)}
                  className="d-flex align-items-center"
                >

                  <Image
                    src={user.avatar || ""}
                    roundedCircle
                    width={40}
                    height={40}
                    className="me-2"
                  />

                  <div>
                    {user.fullName}
                  </div>

                </ListGroup.Item>

              ))}

            </ListGroup>

          )}

          {!loading && !error && searchResults.length === 0 && hasSearched && (

            <div className="text-center text-muted p-3">
              No users found matching "{searchQuery}"
            </div>

          )}

          {!loading && !error && !hasSearched && (

            <div className="text-center text-muted p-3">
              Search for a user to start a conversation
            </div>

          )}

        </div>

      </Modal.Body>

    </Modal>

  );
};

NewChatPopover.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSelectUser: PropTypes.func.isRequired,
};

export default NewChatPopover;