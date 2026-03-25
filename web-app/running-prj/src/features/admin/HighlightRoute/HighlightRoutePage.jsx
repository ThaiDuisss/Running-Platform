import { useEffect, useState, useCallback } from "react";
import axiosClient from "@/shared/services/axiosClient";
import { MapContainer, TileLayer, Marker, Polyline, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import { encode, decode } from "google-polyline";
import simplify from "simplify-js";

// Fix icon lỗi hiển thị của Leaflet trong React
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png",
    iconUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png",
});

const HighlightRoutePage = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [toast, setToast] = useState({ show: false, message: "", type: "success" });

    // Pagination state
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const pageSize = 10;

    // Filter state
    const [filters, setFilters] = useState({
        title: "",
        location: "",
        priority: "",
        isActive: ""
    });

    // Modal & Form state
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState("create");
    const [currentId, setCurrentId] = useState(null);

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [deleteId, setDeleteId] = useState(null);

    const initialFormState = {
        title: "",
        location: "",
        priority: "",
        isActive: true,
        polyline: ""
    };

    const [form, setForm] = useState(initialFormState);
    const [selectedFile, setSelectedFile] = useState(null);
    const [preview, setPreview] = useState(null);
    const [mapPoints, setMapPoints] = useState([]);

    // --- LOGIC BẢN ĐỒ (VẼ ĐƯỜNG THẬT QUA OSRM) ---

    // Hàm gọi API lấy đường đi thực tế giữa 2 điểm
    const fetchRealRoute = async (start, end) => {
        try {
            // Sử dụng profile 'foot' để bám theo đường đi bộ/vỉa hè cho Runwise
            const response = await fetch(
                `https://router.project-osrm.org/route/v1/foot/${start[1]},${start[0]};${end[1]},${end[0]}?overview=full&geometries=geojson`
            );
            const result = await response.json();
            if (result.code === "Ok") {
                // OSRM trả về [lng, lat], cần đảo lại thành [lat, lng] cho Leaflet
                return result.routes[0].geometry.coordinates.map(coord => [coord[1], coord[0]]);
            }
        } catch (error) {
            console.error("Lỗi lấy đường đi thật:", error);
        }
        return [end]; // Fallback trả về điểm đích nếu lỗi
    };

    const MapEvents = () => {
        useMapEvents({
            async click(e) {
                const newClickedPoint = [e.latlng.lat, e.latlng.lng];

                if (mapPoints.length === 0) {
                    setMapPoints([newClickedPoint]);
                } else {
                    const lastPoint = mapPoints[mapPoints.length - 1];
                    // Lấy các điểm thực tế giữa điểm cũ và điểm mới
                    const realSegment = await fetchRealRoute(lastPoint, newClickedPoint);

                    setMapPoints(prev => {
                        // Nối mảng cũ với segment mới (bỏ điểm đầu segment để không trùng với lastPoint)
                        const updated = [...prev, ...realSegment.slice(1)];

                        // Tối ưu hóa polyline trước khi lưu
                        const pointsForSimplify = updated.map(p => ({ x: p[0], y: p[1] }));
                        const simplified = simplify(pointsForSimplify, 0.00002); // Độ nhạy ~5m
                        const finalCoords = simplified.map(p => [p.x, p.y]);

                        // Encode và lưu vào form
                        const encoded = encode(finalCoords, 4); // Dùng precision 5 cho chuẩn Google
                        setForm(f => ({ ...f, polyline: encoded }));

                        return finalCoords;
                    });
                }
            },
        });
        return null;
    };

    const clearRoute = () => {
        setMapPoints([]);
        setForm(f => ({ ...f, polyline: "" }));
    };

    const showMessage = (message, type = "success") => {
        setToast({ show: true, message, type });
        setTimeout(() => setToast({ show: false, message: "", type: "success" }), 3000);
    };

    // ================= FETCH DATA =================
    const fetchData = useCallback(async (page = currentPage) => {
        try {
            setLoading(true);
            let params = { page: page, size: pageSize, ...filters };
            Object.keys(params).forEach(key => {
                if (params[key] === "" || params[key] === null) delete params[key];
            });
            if (params.isActive !== undefined && params.isActive !== "") {
                params.isActive = params.isActive === "true";
            }
            const res = await axiosClient.get("/api/highlight-routes/admin", { params });
            setData(res.data.data.content || []);
            setTotalPages(res.data.data.totalPages || 0);
        } catch (err) {
            console.error("Fetch data failed:", err);
        } finally {
            setLoading(false);
        }
    }, [filters, currentPage]);

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage, fetchData]);

    const handleSearch = () => {
        if (currentPage === 0) fetchData(0);
        else setCurrentPage(0);
    };

    // ================= HANDLERS =================
    const openCreateModal = () => {
        setModalMode("create");
        setForm(initialFormState);
        setMapPoints([]);
        setSelectedFile(null);
        setPreview(null);
        setCurrentId(null);
        setIsModalOpen(true);
    };

    const openEditModal = (item) => {
        setModalMode("update");
        setForm({
            title: item.title || "",
            location: item.location || "",
            priority: item.priority || "",
            isActive: item.isActive,
            polyline: item.polyline || ""
        });

        if (item.polyline) {
            try {
                const decoded = decode(item.polyline);
                setMapPoints(decoded);
            } catch (e) {
                setMapPoints([]);
            }
        } else {
            setMapPoints([]);
        }

        setSelectedFile(null);
        setPreview(item.thumbnail || null);
        setCurrentId(item.id);
        setIsModalOpen(true);
    };

    const closeModal = () => setIsModalOpen(false);

    const handleFileChange = (file) => {
        setSelectedFile(file);
        if (file) setPreview(URL.createObjectURL(file));
    };

    const handleSave = async () => {
        try {
            let savedItem = null;
            if (modalMode === "create") {
                const res = await axiosClient.post("/api/highlight-routes", form);
                savedItem = res.data.data;
            } else {
                const res = await axiosClient.put(`/api/highlight-routes/${currentId}`, form);
                savedItem = res.data.data;
            }

            if (selectedFile && savedItem) {
                const formData = new FormData();
                formData.append("file", selectedFile);
                await axiosClient.post(`/api/highlight-routes/${savedItem.id}/thumbnail`, formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
            }

            showMessage(`Đã ${modalMode === "create" ? "tạo mới" : "cập nhật"} thành công!`);
            closeModal();
            fetchData();
        } catch (err) {
            console.error(err);
            showMessage("Có lỗi xảy ra, vui lòng thử lại", "error");
        }
    };

    const openDeleteModal = (id) => {
        setDeleteId(id);
        setIsDeleteModalOpen(true);
    };

    const closeDeleteModal = () => {
        setDeleteId(null);
        setIsDeleteModalOpen(false);
    };

    const confirmDelete = async () => {
        try {
            await axiosClient.delete(`/api/highlight-routes/${deleteId}`);
            showMessage("Đã xóa tuyến đường thành công!");
            fetchData();
            closeDeleteModal();
        } catch (err) {
            console.error(err);
            showMessage("Xóa thất bại", "error");
        }
    };

    // ================= STYLES (Giữ nguyên của Huy) =================
    const styles = {
        container: { padding: "24px", fontFamily: "'Inter', sans-serif", maxWidth: "1200px", margin: "0 auto", color: "#1f2937" },
        header: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px" },
        filterBox: { display: "flex", gap: "12px", padding: "20px", backgroundColor: "#ffffff", borderRadius: "12px", marginBottom: "24px", flexWrap: "wrap", boxShadow: "0 1px 3px rgba(0,0,0,0.1)", border: "1px solid #e5e7eb" },
        input: { padding: "10px 14px", borderRadius: "8px", border: "1px solid #d1d5db", flex: 1, outline: "none", transition: "border 0.2s" },
        btnPrimary: { padding: "10px 20px", backgroundColor: "#2563eb", color: "#fff", border: "none", borderRadius: "8px", cursor: "pointer", fontWeight: "600", transition: "all 0.2s" },
        btnSecondary: { padding: "10px 20px", backgroundColor: "#fff", color: "#374151", border: "1px solid #d1d5db", borderRadius: "8px", cursor: "pointer", fontWeight: "600" },
        btnDelete: { padding: "10px 20px", backgroundColor: "#ef4444", color: "#fff", border: "none", borderRadius: "8px", cursor: "pointer", fontWeight: "600" },
        table: { width: "100%", borderCollapse: "separate", borderSpacing: "0", marginTop: "10px", borderRadius: "12px", overflow: "hidden", border: "1px solid #e5e7eb" },
        th: { textAlign: "left", padding: "14px", backgroundColor: "#f9fafb", borderBottom: "1px solid #e5e7eb", color: "#6b7280", fontWeight: "600", fontSize: "13px", textTransform: "uppercase" },
        tr: { transition: "background-color 0.2s" },
        td: { padding: "14px", borderBottom: "1px solid #e5e7eb", fontSize: "15px" },
        modalOverlay: { position: "fixed", top: 0, left: 0, right: 0, bottom: 0, backgroundColor: "rgba(0,0,0,0.5)", display: "flex", justifyContent: "center", alignItems: "center", zIndex: 1000, backdropFilter: "blur(4px)" },
        modalContent: { backgroundColor: "#fff", padding: "30px", borderRadius: "16px", width: "100%", maxWidth: "550px", maxHeight: "95vh", overflowY: "auto", boxShadow: "0 20px 25px -5px rgba(0,0,0,0.1)" },
        mapWrapper: { height: "250px", width: "100%", borderRadius: "12px", overflow: "hidden", margin: "10px 0", border: "1px solid #e5e7eb" },
        toast: {
            position: "fixed", top: "20px", right: "20px", padding: "12px 24px", borderRadius: "8px", color: "#fff",
            zIndex: 2000, boxShadow: "0 10px 15px -3px rgba(0,0,0,0.1)", transform: toast.show ? "translateX(0)" : "translateX(150%)",
            transition: "transform 0.3s ease-in-out", display: "flex", alignItems: "center", gap: "10px", fontWeight: "500",
            backgroundColor: toast.type === "success" ? "#10b981" : "#ef4444"
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.toast}>
                {toast.type === "success" ? "✓" : "✕"} {toast.message}
            </div>

            <div style={styles.header}>
                <h2 style={{ fontSize: "24px", fontWeight: "700" }}>Runwise - Highlight Routes</h2>
                <button style={styles.btnPrimary} onClick={openCreateModal}>+ New Route</button>
            </div>

            <div style={styles.filterBox}>
                <input style={styles.input} placeholder="Title..." value={filters.title} onChange={e => setFilters({ ...filters, title: e.target.value })} />
                <input style={styles.input} placeholder="Location..." value={filters.location} onChange={e => setFilters({ ...filters, location: e.target.value })} />
                <select style={styles.input} value={filters.priority} onChange={e => setFilters({ ...filters, priority: e.target.value })}>
                    <option value="">Priority</option>
                    {[1, 2, 3, 4, 5].map(p => <option key={p} value={p}>{p}</option>)}
                </select>
                <select style={styles.input} value={filters.isActive} onChange={e => setFilters({ ...filters, isActive: e.target.value })}>
                    <option value="">Status</option>
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                </select>
                <button style={styles.btnPrimary} onClick={handleSearch}>Search</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        <th style={styles.th}>Image</th>
                        <th style={styles.th}>Title</th>
                        <th style={styles.th}>Location</th>
                        <th style={styles.th}>Priority</th>
                        <th style={styles.th}>Status</th>
                        <th style={styles.th}>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {loading ? (
                        <tr><td colSpan="6" style={{ textAlign: "center", padding: "40px", color: "#6b7280" }}>Đang tải dữ liệu...</td></tr>
                    ) : data.map(item => (
                        <tr key={item.id} style={styles.tr} onMouseOver={(e) => e.currentTarget.style.backgroundColor = "#f9fafb"} onMouseOut={(e) => e.currentTarget.style.backgroundColor = "transparent"}>
                            <td style={styles.td}>
                                <img src={item.thumbnail || "https://via.placeholder.com/50"} alt="thumb" style={{ width: 48, height: 48, borderRadius: 8, objectFit: "cover", border: "1px solid #e5e7eb" }} />
                            </td>
                            <td style={{ ...styles.td, fontWeight: "500" }}>{item.title}</td>
                            <td style={styles.td}>{item.location}</td>
                            <td style={styles.td}><span style={{ padding: "2px 8px", backgroundColor: "#f3f4f6", borderRadius: "4px" }}>{item.priority}</span></td>
                            <td style={styles.td}>
                                <span style={{ padding: "4px 10px", borderRadius: "20px", fontSize: "12px", fontWeight: "600", backgroundColor: item.isActive ? "#ecfdf5" : "#fef2f2", color: item.isActive ? "#059669" : "#dc2626" }}>
                                    {item.isActive ? "Active" : "Inactive"}
                                </span>
                            </td>
                            <td style={styles.td}>
                                <button style={{ padding: "6px 12px", borderRadius: "6px", backgroundColor: "#f59e0b", color: "#fff", border: "none", cursor: "pointer", marginRight: "8px" }} onClick={() => openEditModal(item)}>Edit</button>
                                <button style={{ padding: "6px 12px", borderRadius: "6px", backgroundColor: "#ef4444", color: "#fff", border: "none", cursor: "pointer" }} onClick={() => openDeleteModal(item.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {!loading && totalPages > 1 && (
                <div style={{ display: "flex", justifyContent: "center", marginTop: "24px", gap: "12px", alignItems: "center" }}>
                    <button style={{ ...styles.btnSecondary, padding: "6px 16px", cursor: currentPage === 0 ? "not-allowed" : "pointer", opacity: currentPage === 0 ? 0.5 : 1 }} disabled={currentPage === 0} onClick={() => setCurrentPage(p => p - 1)}>Prev</button>
                    <span style={{ fontWeight: "600" }}>Trang {currentPage + 1} / {totalPages}</span>
                    <button style={{ ...styles.btnSecondary, padding: "6px 16px", cursor: currentPage >= totalPages - 1 ? "not-allowed" : "pointer", opacity: currentPage >= totalPages - 1 ? 0.5 : 1 }} disabled={currentPage >= totalPages - 1} onClick={() => setCurrentPage(p => p + 1)}>Next</button>
                </div>
            )}

            {isModalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modalContent}>
                        <h3 style={{ marginBottom: "20px", fontSize: "20px" }}>{modalMode === "create" ? "Tạo tuyến đường mới" : "Cập nhật tuyến đường"}</h3>

                        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <label style={{ fontWeight: "600" }}>Lộ trình (Click để chọn các điểm)</label>
                            <button onClick={clearRoute} style={{ fontSize: "12px", color: "#ef4444", cursor: "pointer", background: "none", border: "1px solid #ef4444", borderRadius: "4px", padding: "2px 6px" }}>Xóa lộ trình</button>
                        </div>
                        <div style={styles.mapWrapper}>
                            <MapContainer center={mapPoints.length > 0 ? mapPoints[0] : [21.0285, 105.8542]} zoom={13} style={{ height: "100%" }}>
                                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                                <MapEvents />
                                {mapPoints.length > 0 && (
                                    <>
                                        <Marker position={mapPoints[0]} />
                                        <Marker position={mapPoints[mapPoints.length - 1]} />
                                        <Polyline positions={mapPoints} color="#2563eb" weight={4} />
                                    </>
                                )}
                            </MapContainer>
                        </div>

                        <div style={{ marginBottom: "15px" }}>
                            <label style={{ display: "block", marginBottom: "6px", fontWeight: "600" }}>Title</label>
                            <input style={{ ...styles.input, width: "100%", boxSizing: "border-box" }} placeholder="VD: Hồ Tây sáng sớm..." value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} />
                        </div>

                        <div style={{ display: "flex", gap: "15px", marginBottom: "15px" }}>
                            <div style={{ flex: 1 }}>
                                <label style={{ display: "block", marginBottom: "6px", fontWeight: "600" }}>Location</label>
                                <input style={{ ...styles.input, width: "100%", boxSizing: "border-box" }} placeholder="Hà Nội..." value={form.location} onChange={e => setForm({ ...form, location: e.target.value })} />
                            </div>
                            <div style={{ flex: 1 }}>
                                <label style={{ display: "block", marginBottom: "6px", fontWeight: "600" }}>Priority (1-5)</label>
                                <input type="number" style={{ ...styles.input, width: "100%", boxSizing: "border-box" }} value={form.priority} onChange={e => setForm({ ...form, priority: e.target.value })} />
                            </div>
                        </div>

                        <div style={{ marginBottom: "15px" }}>
                            <label style={{ display: "block", marginBottom: "6px", fontWeight: "600" }}>Status</label>
                            <select style={{ ...styles.input, width: "100%", boxSizing: "border-box" }} value={form.isActive} onChange={e => setForm({ ...form, isActive: e.target.value === "true" })}>
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>
                        </div>

                        <div style={{ marginBottom: "20px" }}>
                            <label style={{ display: "block", marginBottom: "6px", fontWeight: "600" }}>Thumbnail</label>
                            <input type="file" accept="image/*" onChange={e => handleFileChange(e.target.files[0])} />
                            {preview && <img src={preview} alt="preview" style={{ width: "100%", height: "160px", objectFit: "cover", marginTop: "12px", borderRadius: "8px", border: "1px solid #e5e7eb" }} />}
                        </div>

                        <div style={{ display: "flex", justifyContent: "flex-end", gap: "12px" }}>
                            <button style={styles.btnSecondary} onClick={closeModal}>Hủy</button>
                            <button style={styles.btnPrimary} onClick={handleSave}>Xác nhận</button>
                        </div>
                    </div>
                </div>
            )}

            {isDeleteModalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={{ ...styles.modalContent, maxWidth: "400px", textAlign: "center" }}>
                        <div style={{ fontSize: "50px", color: "#ef4444", marginBottom: "10px" }}>⚠</div>
                        <h3 style={{ marginBottom: "10px", fontSize: "20px" }}>Xác nhận xóa</h3>
                        <p style={{ color: "#6b7280", marginBottom: "25px" }}>Bạn có chắc chắn muốn xóa tuyến đường này không?</p>
                        <div style={{ display: "flex", justifyContent: "center", gap: "12px" }}>
                            <button style={styles.btnSecondary} onClick={closeDeleteModal}>Hủy</button>
                            <button style={styles.btnDelete} onClick={confirmDelete}>Xóa ngay</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default HighlightRoutePage;