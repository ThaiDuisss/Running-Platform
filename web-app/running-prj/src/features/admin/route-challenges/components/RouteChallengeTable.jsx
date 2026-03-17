import { Table, Button, Badge } from "react-bootstrap";
import { Pencil, Trash } from "react-bootstrap-icons";

const RouteChallengeTable = ({ challenges, onEdit, onDelete }) => {
    const getStatusBadge = (status) => {
        switch (status) {
            case "ACTIVE":
                return <Badge bg="success">Active</Badge>;
            case "COMPLETED":
                return <Badge bg="primary">Completed</Badge>;
            case "CANCELLED":
                return <Badge bg="danger">Cancelled</Badge>;
            default:
                return <Badge bg="secondary">Unknown</Badge>;
        }
    };

    const getVisibilityBadge = (visibility) => {
        switch (visibility) {
            case "PUBLIC":
                return <Badge bg="info">Public</Badge>;
            case "PRIVATE":
                return <Badge bg="warning">Private</Badge>;
            case "FRIEND":
                return <Badge bg="secondary">Friend</Badge>;
            default:
                return <Badge bg="light">Unknown</Badge>;
        }
    };

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Type</th>
                    <th>Target Value</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                    <th>Visibility</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {Array.isArray(challenges) && challenges.map((challenge) => (
                    <tr key={challenge.id}>
                        <td>{challenge.id}</td>
                        <td>{challenge.title}</td>
                        <td>{challenge.type ?? challenge.challengeType ?? "-"}</td>
                        <td>{challenge.targetValue}</td>
                        <td>{new Date(challenge.startTime).toLocaleString()}</td>
                        <td>{new Date(challenge.endTime).toLocaleString()}</td>
                        <td>{getVisibilityBadge(challenge.visibility)}</td>
                        <td>{getStatusBadge(challenge.status)}</td>
                        <td>
                            <Button
                                variant="outline-primary"
                                size="sm"
                                onClick={() => onEdit(challenge)}
                                className="me-2"
                            >
                                <Pencil />
                            </Button>
                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={() => onDelete(challenge)}
                            >
                                <Trash />
                            </Button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );
};

export default RouteChallengeTable