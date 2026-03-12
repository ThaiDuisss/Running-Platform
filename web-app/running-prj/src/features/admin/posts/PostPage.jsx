import { useState, useEffect } from "react";
import axios from "axios";
import { FaTrash } from "react-icons/fa";

function PostPage() {
    const [posts, setPosts] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [statusFilter, setStatusFilter] = useState("");
    const [loading, setLoading] = useState(false);

    const API_BASE_URL = "http://localhost:8080/api/admin/posts";

    useEffect(() => {
        fetchPosts();
    }, []);

    const fetchPosts = async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem("ACCESS-TOKEN");

            const response = await axios.get(
                `${API_BASE_URL}?page=0&size=50`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            const data = response.data?.data?.content || [];
            setPosts(data);
        } catch (error) {
            console.error("Fail to load posts:", error);
            alert("Cannot load posts from server");
        } finally {
            setLoading(false);
        }
    };

    const handleApprove = async (id) => {
        if (!window.confirm("Are you sure you want to approve this post?")) return;

        try {
            const token = localStorage.getItem("ACCESS-TOKEN");

            await axios.put(
                `${API_BASE_URL}/${id}/approve`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            fetchPosts();
        } catch (error) {
            console.error("Approve failed:", error);
            alert("Approve failed");
        }
    };

    const handleReject = async (id) => {
        if (!window.confirm("Are you sure you want to reject this post?")) return;

        try {
            const token = localStorage.getItem("ACCESS-TOKEN");

            await axios.put(
                `${API_BASE_URL}/${id}/reject`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            fetchPosts();
        } catch (error) {
            console.error("Reject failed:", error);
            alert("Reject failed");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this post?")) return;

        try {
            const token = localStorage.getItem("ACCESS-TOKEN");

            await axios.delete(`${API_BASE_URL}/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            fetchPosts();
        } catch (error) {
            console.error("Delete failed:", error);
            alert("Delete failed");
        }
    };

    const filteredPosts = Array.isArray(posts)
        ? posts.filter(post =>
            (post.content || "")
                .toLowerCase()
                .includes(keyword.toLowerCase()) &&
            (statusFilter === "" || post.status === statusFilter)
        )
        : [];

    return (
        <div className="container mt-4">

            {/* Search + Filter */}
            <div className="d-flex gap-2 mb-3">
                <input
                    className="form-control"
                    placeholder="Search post content..."
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                />

                <select
                    className="form-select"
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                >
                    <option value="">All Status</option>
                    <option value="PENDING">Pending</option>
                    <option value="APPROVE">Approve</option>
                    <option value="REJECT">Reject</option>
                </select>
            </div>

            {/* Loading */}
            {loading ? (
                <p>Loading posts...</p>
            ) : (
                <table className="table table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th>Content</th>
                            <th>User</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>
                        {filteredPosts.length === 0 ? (
                            <tr>
                                <td colSpan="5" className="text-center">
                                    No posts found
                                </td>
                            </tr>
                        ) : (
                            filteredPosts.map(post => (
                                <tr key={post.id}>

                                    <td>{post.content}</td>

                                    <td>{post.username}</td>

                                    <td>
                                        <span
                                            className={`badge ${post.status === "APPROVE"
                                                    ? "bg-success"
                                                    : post.status === "REJECT"
                                                        ? "bg-danger"
                                                        : "bg-warning text-dark"
                                                }`}
                                        >
                                            {post.status}
                                        </span>
                                    </td>

                                    <td>
                                        <div className="d-flex gap-2">

                                            {post.status === "PENDING" && (
                                                <button
                                                    className="btn btn-success btn-sm"
                                                    onClick={() => handleApprove(post.id)}
                                                >
                                                    Approve
                                                </button>
                                            )}

                                            {post.status === "PENDING" && (
                                                <button
                                                    className="btn btn-warning btn-sm"
                                                    onClick={() => handleReject(post.id)}
                                                >
                                                    Reject
                                                </button>
                                            )}

                                            <button
                                                className="btn btn-danger btn-sm"
                                                onClick={() => handleDelete(post.id)}
                                            >
                                                <FaTrash />
                                            </button>

                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default PostPage;