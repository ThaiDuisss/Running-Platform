import { useState } from "react";
import Table from "react-bootstrap/Table";
import { FaEdit, FaTrash } from "react-icons/fa";
import "./UserTable.css";

const UserTable = (props) => {
    const { users, loading, handleUpdateUser, handleDeleteUser } = props

    return (
        <div>

            <h3 />
            <Table striped bordered hover className="user-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Avatar</th>
                        <th>Name</th>
                        <th>Phone Number</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>

                    {loading && users.length === 0 ? (

                        <tr>
                            <td colSpan="4" style={{ textAlign: "center" }}>
                                Loading...
                            </td>
                        </tr>

                    ) : users.length > 0 ? (

                        users.map(user => (

                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td><img src={user.imageUrl} alt="avatar" /></td>
                                <td>{user.username}</td>
                                <td>{user.phoneNumber}</td>
                                <td>{user.roles?.[0]?.roleName}</td>                                <td>
                                    <div className="action-buttons">
                                        <i
                                            className="bi bi-pencil-square action-icon icon-edit"
                                            onClick={() => handleUpdateUser(user)}
                                        ></i>

                                        <i
                                            className="bi bi-trash action-icon icon-delete"
                                            onClick={() => handleDeleteUser(user.id)}
                                        ></i>
                                    </div>
                                </td>
                            </tr>

                        ))

                    ) : (

                        <tr>
                            <td colSpan="4" style={{ textAlign: "center" }}>
                                No user
                            </td>
                        </tr>

                    )}

                </tbody>

            </Table>
        </div>
    );
};

export default UserTable;