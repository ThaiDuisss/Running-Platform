import { useState, useEffect } from "react";
import { FaTrashAlt, FaCheck, FaTimes, FaEye } from "react-icons/fa";
import {
  changeApproveStatusPostAPI,
  getPostsWithPaginateAPI,
  changeRejectStatusPostAPI,
  deletePostAPI
} from "../users/services/UserService";
import "./PostPage.css";

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
    <div className="post-page">
      {/* HEADER */}
      <div className="page-header">
        <h4>Quản lý bài viết</h4>
      </div>

      {/* FILTER - Đã sửa để chia đều không gian */}
      <div className="filter-bar">
        <div className="filter-item">
          <input
            className="form-control"
            placeholder="Tìm kiếm nội dung..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>
        <div className="filter-item">
          <select
            className="form-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">Tất cả trạng thái</option>
            <option value="PENDING">PENDING</option>
            <option value="APPROVE">APPROVE</option>
            <option value="REJECT">REJECT</option>
          </select>
        </div>
      </div>

      {/* CONTENT */}
      {loading ? (
        <div className="loading text-center" style={{ padding: '50px', color: '#666' }}>
          Đang tải dữ liệu...
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="custom-table">
            <thead>
              <tr>
                <th style={{ width: '40%' }}>Nội dung</th>
                <th style={{ width: '20%', textAlign: 'center' }}>Người đăng</th>
                <th style={{ width: '15%', textAlign: 'center' }}>Trạng thái</th>
                <th style={{ width: '25%', textAlign: 'center' }}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {filteredPosts.length === 0 ? (
                <tr>
                  <td colSpan="4" className="text-center empty" style={{ padding: '50px' }}>
                    Không tìm thấy bài viết nào
                  </td>
                </tr>
              ) : (
                filteredPosts.map(post => (
                  <tr key={post.id}>
                    <td className="content-cell" title={post.content}>
                      {post.content}
                    </td>
                    <td className="user-cell align-middle">
                      <div className="user-info">
                        <div className="avatar">
                          {post.username?.charAt(0).toUpperCase()}
                        </div>
                        <span>{post.username}</span>
                      </div>
                    </td>
                    <td className="text-center align-middle">
                      <span className={`status ${(post.status || "").toLowerCase()}`}>
                        {post.status}
                      </span>
                    </td>
                    <td className="align-middle">
                      <div className="action-group" style={{ justifyContent: 'center' }}>
                        {post.status === "PENDING" && (
                          <>
                            <button className="action-btn approve" onClick={() => handleApprove(post.id)} title="Duyệt">
                              <FaCheck />
                            </button>
                            <button className="action-btn reject" onClick={() => handleReject(post.id)} title="Từ chối">
                              <FaTimes />
                            </button>
                          </>
                        )}
                        <button className="action-btn delete" onClick={() => handleDelete(post.id)} title="Xóa">
                          <FaTrashAlt />
                        </button>
                        <button className="action-btn detail" onClick={() => handleViewDetails(post)} title="Chi tiết">
                          <FaEye />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* MODAL */}
      {showModal && selectedPost && (
        <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-box" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h5 style={{ margin: 0, fontSize: '18px', fontWeight: 600 }}>Chi tiết bài viết</h5>
              <button className="btn-close" onClick={() => setShowModal(false)}>&times;</button>
            </div>
            <div className="modal-body">
              <p><strong>Người đăng:</strong> {selectedPost.username}</p>
              <p>
                <strong>Trạng thái:</strong>
                <span className={`status ${selectedPost.status.toLowerCase()}`} style={{ marginLeft: '10px' }}>
                  {selectedPost.status}
                </span>
              </p>
              <p><strong>Nội dung:</strong></p>
              <div className="content-box">
                {selectedPost.content}
              </div>
              {selectedPost.images?.length > 0 && (
                <div className="image-grid">
                  {selectedPost.images.map((img, i) => (
                    <img key={i} src={img} alt="post" />
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default PostPage;