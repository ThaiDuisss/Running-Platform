import React, { useState, useEffect, useRef, useCallback, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Card,
  Form,
  Button,
  ListGroup,
  Badge,
  Spinner,
  Alert,
  Image
} from "react-bootstrap";

import Scene from "./Scene";
import NewChatPopover from "./NewChatPopover";
import { connectSocket, getSocket } from "@/shared/services/serverSocket";
import {
  getMyConversations,
  createConversation,
  getMessages,
  createMessage,
} from "../services/chatService";
import { AuthDataContext } from "@/app/providers/AuthProvider";


export default function Chat() {

  const [message, setMessage] = useState("");
  const [newChatAnchorEl, setNewChatAnchorEl] = useState(null);
  const [conversations, setConversations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedConversation, setSelectedConversation] = useState(null);
  const [messagesMap, setMessagesMap] = useState({});

  const messageContainerRef = useRef(null);

  const navigate = useNavigate();
  const { user } = useContext(AuthDataContext);

  const scrollToBottom = useCallback(() => {
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop =
        messageContainerRef.current.scrollHeight;
    }
  }, []);

  useEffect(() => {
    connectSocket(null, 8089);
    const socket = getSocket();

    socket.on("message", (message) => {
      const messageObject = JSON.parse(message);

      if (messageObject?.conversationId) {
        handleIncomingMessage(messageObject);
      }
    });

  }, []);

  const handleIncomingMessage = useCallback((message) => {

    setMessagesMap((prev) => {

      const existingMessages = prev[message.conversationId] || [];

      const exists = existingMessages.some((msg) => msg.id === message.id);

      if (!exists) {
        return {
          ...prev,
          [message.conversationId]: [...existingMessages, message]
        };
      }

      return prev;
    });

  }, []);

  const fetchConversations = async () => {
    setLoading(true);
    try {
      const response = await getMyConversations();
      setConversations(response?.data || []);
    } catch (err) {
      setError("Failed to load conversations");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {

    if (!user) {
      navigate("/login");
    }

    fetchConversations();

  }, []);

  useEffect(() => {

    if (conversations.length > 0 && !selectedConversation) {
      setSelectedConversation(conversations[0]);
    }

  }, [conversations]);

  useEffect(() => {

    const fetchMessages = async (conversationId) => {

      if (!messagesMap[conversationId]) {

        const response = await getMessages(conversationId);

        if (response?.data) {

          setMessagesMap((prev) => ({
            ...prev,
            [conversationId]: response.data
          }));
        }
      }

    };

    if (selectedConversation?.id) {
      fetchMessages(selectedConversation.id);
    }

  }, [selectedConversation]);

  const currentMessages = selectedConversation
    ? messagesMap[selectedConversation.id] || []
    : [];

  useEffect(() => {
    scrollToBottom();
  }, [currentMessages]);

  const handleSendMessage = async () => {

    if (!message.trim() || !selectedConversation) return;

    const msg = message;
    setMessage("");

    try {

      await createMessage({
        conversationId: selectedConversation.id,
        message: msg
      });

    } catch (error) {
      console.error(error);
    }

  };

  return (

    <Scene>

      <Container fluid className="h-100">

        <Row style={{ height: "calc(100vh - 64px)" }}>

          {/* LEFT SIDE */}

          <Col md={3} className="border-end d-flex flex-column">

            <div className="p-3 border-bottom d-flex justify-content-between">

              <h5>Chats</h5>

              <Button size="sm">
                +
              </Button>

            </div>

            <div className="flex-grow-1 overflow-auto">

              {loading && (
                <div className="text-center p-3">
                  <Spinner animation="border" />
                </div>
              )}

              {error && (
                <Alert variant="danger">
                  {error}
                </Alert>
              )}

              <ListGroup variant="flush">

                {conversations.map((conv) => (

                  <ListGroup.Item
                    key={conv.id}
                    action
                    active={selectedConversation?.id === conv.id}
                    onClick={() => setSelectedConversation(conv)}
                  >

                    <div className="d-flex align-items-center">

                      <Image
                        src={conv.conversationAvatar}
                        roundedCircle
                        width={40}
                        height={40}
                        className="me-2"
                      />

                      <div className="flex-grow-1">

                        <div className="fw-bold">
                          {conv.conversationName}
                        </div>

                        <small>
                          {conv.lastMessage}
                        </small>

                      </div>

                      {conv.unread > 0 && (
                        <Badge bg="danger">
                          {conv.unread}
                        </Badge>
                      )}

                    </div>

                  </ListGroup.Item>

                ))}

              </ListGroup>

            </div>

          </Col>


          {/* CHAT AREA */}

          <Col md={9} className="d-flex flex-column">

            {selectedConversation && (

              <>
                <div className="border-bottom p-3 d-flex align-items-center">

                  <Image
                    src={selectedConversation.conversationAvatar}
                    roundedCircle
                    width={40}
                    height={40}
                    className="me-2"
                  />

                  <h5 className="mb-0">
                    {selectedConversation.conversationName}
                  </h5>

                </div>

                {/* MESSAGE LIST */}

                <div
                  ref={messageContainerRef}
                  className="flex-grow-1 overflow-auto p-3"
                >

                  {currentMessages.map((msg) => (

                    <div
                      key={msg.id}
                      className={`d-flex mb-3 ${msg.me ? "justify-content-end" : "justify-content-start"}`}
                    >

                      {!msg.me && (
                        <Image
                          src={msg.sender?.avatar}
                          roundedCircle
                          width={32}
                          height={32}
                          className="me-2"
                        />
                      )}

                      <Card
                        className="p-2"
                        style={{
                          maxWidth: "70%",
                          background: msg.me ? "#d0e7ff" : "#f1f1f1"
                        }}
                      >

                        <div>{msg.message}</div>

                        <small className="text-muted text-end">
                          {new Date(msg.createDate * 1000).toLocaleString()}
                        </small>

                      </Card>

                    </div>

                  ))}

                </div>

                {/* INPUT */}

                <Form
                  onSubmit={(e) => {
                    e.preventDefault();
                    handleSendMessage();
                  }}
                  className="border-top p-3 d-flex"
                >

                  <Form.Control
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Type a message"
                  />

                  <Button
                    type="submit"
                    className="ms-2"
                    disabled={!message.trim()}
                  >
                    Send
                  </Button>

                </Form>

              </>
            )}

          </Col>

        </Row>

      </Container>

    </Scene>

  );
}