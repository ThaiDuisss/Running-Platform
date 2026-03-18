import { useState, useEffect } from "react";
import { FaTrashAlt, FaCheck, FaTimes, FaEye } from "react-icons/fa";
import {
  changeApproveStatusPostAPI,
  getPostsWithPaginateAPI,
  changeRejectStatusPostAPI,
  deletePostAPI
} from "../users/services/UserService";

function PostPage() {

  const [posts, setPosts] = useState([]);
  const [keyword, setKeyword] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [selectedPost, setSelectedPost] = useState(null);

  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = async () => {
    try {
      setLoading(true);

      const res = await getPostsWithPaginateAPI(0, 1000, "", "");
      const data = res.data.data;

      setPosts(data.content || data);

    } catch (error) {
      console.error("Fail to load posts:", error);
      alert("Cannot load posts from server");
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (id) => {
    try {
      await changeApproveStatusPostAPI(id);
      fetchPosts();
    } catch (error) {
      console.error("Approve failed:", error);
      alert("Approve failed");
    }
  };

  const handleReject = async (id) => {
    try {
      await changeRejectStatusPostAPI(id);
      fetchPosts();
    } catch (error) {
      console.error("Reject failed:", error);
      alert("Reject failed");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this post?")) return;

    try {
      await deletePostAPI(id);
      fetchPosts();
    } catch (error) {
      console.error("Delete failed:", error);
      alert("Delete failed");
    }
  };

  const handleViewDetails = (post) => {
    setSelectedPost(post);
    setShowModal(true);
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

      {/* Content */}
      {loading ? (
        <p>Loading posts...</p>
      ) : (
        <>
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
                  <td colSpan="4" className="text-center">
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
                      <div className="action-group">

                        {post.status === "PENDING" ? (
                          <button
                            className="action-btn approve"
                            onClick={() => handleApprove(post.id)}
                          >
                            <FaCheck />
                          </button>
                        ) : <div/>}

                        {post.status === "PENDING" ? (
                          <button
                            className="action-btn reject"
                            onClick={() => handleReject(post.id)}
                          >
                            <FaTimes />
                          </button>
                        ) : <div/>}

                        <button
                          className="action-btn delete"
                          onClick={() => handleDelete(post.id)}
                        >
                          <FaTrashAlt />
                        </button>

                        <button
                          className="action-btn detail"
                          onClick={() => handleViewDetails(post)}
                        >
                          <FaEye />
                        </button>

                      </div>
                    </td>

                  </tr>
                ))
              )}
            </tbody>

          </table>

          {/* MODAL */}
          {showModal && selectedPost && (
            <div
              className="modal fade show"
              style={{ display: "block", background: "rgba(0,0,0,0.5)" }}
              onClick={(e) => e.target === e.currentTarget && setShowModal(false)}
            >
              <div className="modal-dialog modal-lg">
                <div className="modal-content">

                  <div className="modal-header">
                    <h5 className="modal-title">Post Detail</h5>
                    <button
                      className="btn-close"
                      onClick={() => setShowModal(false)}
                    ></button>
                  </div>

                  <div className="modal-body">
                    <p><strong>User:</strong> {selectedPost.username}</p>
                    <p><strong>Status:</strong> {selectedPost.status}</p>

                    <p><strong>Content:</strong></p>
                    <div style={{ marginBottom: 10 }}>
                      {selectedPost.content}
                    </div>

                    {selectedPost.images?.length > 0 && (
                      <div
                        style={{
                          display: "grid",
                          gridTemplateColumns: "repeat(2, 1fr)",
                          gap: 10
                        }}
                      >
                        {selectedPost.images.map((img, index) => (
                          <img
                            key={index}
                            src={img}
                            alt="post"
                            style={{
                              width: "100%",
                              borderRadius: 8
                            }}
                          />
                        ))}
                      </div>
                    )}
                  </div>

                  <div className="modal-footer">
                    <button
                      className="btn btn-secondary"
                      onClick={() => setShowModal(false)}
                    >
                      Close
                    </button>
                  </div>

                </div>
              </div>
            </div>
          )}

        </>
      )}

    </div>
  );
}

export default PostPage;