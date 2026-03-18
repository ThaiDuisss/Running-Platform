import React, { useEffect, useMemo, useState } from "react";
import { Button, Card, Col, Container, Form, Row, Spinner, Table, Tab, Tabs } from "react-bootstrap";
import { toast } from "react-toastify";
import PaginationCustom from "@/features/admin/users/components/Pagination";
import { friendService } from "@/app/services/FriendService";

const FRIENDS_TAB = "friends";
const SENT_TAB = "sent";
const RECEIVED_TAB = "received";

const FriendPage = () => {
    const [activeTab, setActiveTab] = useState(FRIENDS_TAB);

    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);

    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [newFriendId, setNewFriendId] = useState("");

    const resetPagination = () => {
        setCurrentPage(0);
        setTotalPages(0);
        setTotalElements(0);
    };

    const loadData = async () => {
        setLoading(true);

        try {
            let res;
            if (activeTab === FRIENDS_TAB) {
                res = await friendService.getFriends(currentPage, pageSize);
            } else if (activeTab === SENT_TAB) {
                res = await friendService.getSentRequests(currentPage, pageSize);
            } else {
                res = await friendService.getReceivedRequests(currentPage, pageSize);
            }

            const pageData = res?.data?.data;
            setItems(pageData?.content || []);
            setTotalPages(pageData?.totalPages || 0);
            setTotalElements(pageData?.totalElements || 0);
        } catch (error) {
            console.error(error);
            toast.error("Failed to fetch data");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, [activeTab, currentPage, pageSize]);

    const handleTabSelect = (tabKey) => {
        setActiveTab(tabKey);
        resetPagination();
    };

    const handleFriendAction = async (action, item) => {
        try {
            switch (action) {
                case "unfriend":
                    await friendService.unfriend(item.id);
                    toast.success("Unfriended");
                    break;
                case "cancel":
                    await friendService.cancelRequest(item.userId);
                    toast.success("Request cancelled");
                    break;
                case "accept":
                    await friendService.acceptRequest(item.id);
                    toast.success("Request accepted");
                    break;
                case "reject":
                    await friendService.rejectRequest(item.id);
                    toast.success("Request rejected");
                    break;
                default:
                    break;
            }

            loadData();
        } catch (error) {
            console.error(error);
            toast.error("Action failed");
        }
    };

    const handleSendRequest = async () => {
        if (!newFriendId) {
            toast.warn("Please enter a user id to send a request");
            return;
        }

        try {
            await friendService.sendRequest(Number(newFriendId));
            toast.success("Friend request sent");
            setNewFriendId("");
            if (activeTab === SENT_TAB) {
                loadData();
            }
        } catch (error) {
            console.error(error);
            toast.error("Failed to send request");
        }
    };

    const tableRows = useMemo(() => {
        if (loading) {
            return (
                <tr>
                    <td colSpan={5} className="text-center py-4">
                        <Spinner animation="border" size="sm" /> Loading...
                    </td>
                </tr>
            );
        }

        if (!items?.length) {
            return (
                <tr>
                    <td colSpan={5} className="text-center py-4">
                        No data
                    </td>
                </tr>
            );
        }

        return items.map((item) => {
            const avatarUrl = item.imageUrl || "https://i.pravatar.cc/40";

            return (
                <tr key={item.id}>
                    <td>
                        <img
                            src={avatarUrl}
                            alt="avatar"
                            className="rounded-circle"
                            style={{ width: 40, height: 40, objectFit: "cover" }}
                        />
                    </td>
                    <td>{item.username}</td>
                    <td>{item.fullName}</td>
                    <td>{item.userId ? `ID: ${item.userId}` : null}</td>
                    <td>
                        {activeTab === FRIENDS_TAB && (
                            <Button
                                size="sm"
                                variant="outline-danger"
                                onClick={() => handleFriendAction("unfriend", item)}
                            >
                                Unfriend
                            </Button>
                        )}

                        {activeTab === SENT_TAB && (
                            <Button
                                size="sm"
                                variant="outline-secondary"
                                onClick={() => handleFriendAction("cancel", item)}
                            >
                                Cancel
                            </Button>
                        )}

                        {activeTab === RECEIVED_TAB && (
                            <>
                                <Button
                                    size="sm"
                                    variant="success"
                                    className="me-2"
                                    onClick={() => handleFriendAction("accept", item)}
                                >
                                    Accept
                                </Button>
                                <Button
                                    size="sm"
                                    variant="outline-danger"
                                    onClick={() => handleFriendAction("reject", item)}
                                >
                                    Reject
                                </Button>
                            </>
                        )}
                    </td>
                </tr>
            );
        });
    }, [activeTab, items, loading]);

    return (
        <Container className="py-4">
            <Card className="shadow-sm">
                <Card.Body>
                    <h4 className="mb-4">Friends</h4>

                    <Row className="mb-3">
                        <Col md={6}>
                            <Form.Group>
                                <Form.Label>Send friend request by User ID</Form.Label>
                                <div className="d-flex gap-2">
                                    <Form.Control
                                        value={newFriendId}
                                        onChange={(e) => setNewFriendId(e.target.value)}
                                        placeholder="Enter user id"
                                    />
                                    <Button onClick={handleSendRequest}>Send</Button>
                                </div>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Tabs activeKey={activeTab} onSelect={handleTabSelect} className="mb-3">
                        <Tab eventKey={FRIENDS_TAB} title={`Friends (${totalElements})`} />
                        <Tab eventKey={SENT_TAB} title={`Sent (${totalElements})`} />
                        <Tab eventKey={RECEIVED_TAB} title={`Received (${totalElements})`} />
                    </Tabs>

                    <Table hover responsive>
                        <thead>
                            <tr>
                                <th>Avatar</th>
                                <th>Username</th>
                                <th>Full name</th>
                                <th>ID</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>{tableRows}</tbody>
                    </Table>

                    {totalPages > 0 && (
                        <PaginationCustom
                            currentPage={currentPage}
                            totalPages={totalPages}
                            totalElements={totalElements}
                            onPageChange={setCurrentPage}
                        />
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default FriendPage;
