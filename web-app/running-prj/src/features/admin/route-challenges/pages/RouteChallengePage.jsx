import { useEffect, useState } from "react";
import { Button, Container, Row, Col, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";
import RouteChallengeTable from "../components/RouteChallengeTable";
import CreateRouteChallengeModal from "../components/CreateRouteChallengeModal";
import UpdateRouteChallengeModal from "../components/UpdateRouteChallengeModal";
import DeleteConfirmModal from "../components/DeleteConfirmModal"; // Assume you have this
import {
    createRouteChallengeAPI,
    getRouteChallengesWithPaginateAPI,
    updateRouteChallengeAPI,
    deleteRouteChallengeAPI,
} from "../services/RouteChallengeService";

const RouteChallengePage = () => {
    const [challenges, setChallenges] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);

    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showUpdateModal, setShowUpdateModal] = useState(false);
    const [selectedChallenge, setSelectedChallenge] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [challengeToDelete, setChallengeToDelete] = useState(null);

    const fetchChallenges = async () => {
        try {
            setLoading(true);
            const res = await getRouteChallengesWithPaginateAPI(currentPage, pageSize);
            const data = res.data.data;
            setChallenges(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            toast.error("Failed to fetch challenges");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchChallenges();
    }, [currentPage]);

    const handleCreate = async (data) => {
        try {
            await createRouteChallengeAPI(data);
            toast.success("Challenge created successfully");
            fetchChallenges();
        } catch (error) {
            toast.error("Failed to create challenge");
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateRouteChallengeAPI(id, data);
            toast.success("Challenge updated successfully");
            fetchChallenges();
        } catch (error) {
            toast.error("Failed to update challenge");
        }
    };

    const handleDelete = async () => {
        try {
            await deleteRouteChallengeAPI(challengeToDelete.id);
            toast.success("Challenge deleted successfully");
            fetchChallenges();
            setShowDeleteModal(false);
        } catch (error) {
            toast.error("Failed to delete challenge");
        }
    };

    const onEdit = (challenge) => {
        setSelectedChallenge(challenge);
        setShowUpdateModal(true);
    };

    const onDelete = (challenge) => {
        setChallengeToDelete(challenge);
        setShowDeleteModal(true);
    };

    return (
        <Container>
            <Row className="mb-3">
                <Col>
                    <h2>Route Challenges</h2>
                </Col>
                <Col className="text-end">
                    <Button onClick={() => setShowCreateModal(true)}>Create New Challenge</Button>
                </Col>
            </Row>
            {loading ? (
                <Spinner animation="border" />
            ) : (
                <RouteChallengeTable
                    challenges={challenges}
                    onEdit={onEdit}
                    onDelete={onDelete}
                />
            )}
            {/* Pagination can be added later */}
            <CreateRouteChallengeModal
                show={showCreateModal}
                onClose={() => setShowCreateModal(false)}
                onCreate={handleCreate}
            />
            <UpdateRouteChallengeModal
                show={showUpdateModal}
                onClose={() => setShowUpdateModal(false)}
                onUpdate={handleUpdate}
                challenge={selectedChallenge}
            />
            <DeleteConfirmModal
                show={showDeleteModal}
                onClose={() => setShowDeleteModal(false)}
                onConfirm={handleDelete}
                itemName="Route Challenge"
                itemTitle={challengeToDelete?.title}
            />
        </Container>
    );
};

export default RouteChallengePage;