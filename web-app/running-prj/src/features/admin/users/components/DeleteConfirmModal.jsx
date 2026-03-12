import { Modal, Button } from "react-bootstrap";

const DeleteConfirmModal = ({ show, onClose, onConfirm, user }) => {

    return (
        <Modal show={show} onHide={onClose} centered>

            <Modal.Header closeButton>
                <Modal.Title>Confirm Delete</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                Are you sure you want to delete user <b>{user?.username}</b>?
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Cancel
                </Button>

                <Button variant="danger" onClick={onConfirm}>
                    Delete
                </Button>
            </Modal.Footer>

        </Modal>
    );
};

export default DeleteConfirmModal;