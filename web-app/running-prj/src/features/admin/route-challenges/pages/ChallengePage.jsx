// import { useEffect, useState, useRef } from "react";
// import axiosClient from "@/shared/services/axiosClient";
// import L from "leaflet";
// import "leaflet/dist/leaflet.css";

// const ChallengePage = () => {
//     const [activeTab, setActiveTab] = useState("list");
//     const [challenges, setChallenges] = useState([]);
//     const [loading, setLoading] = useState(false);
//     const [notification, setNotification] = useState({ type: "", message: "" });

//     const [visibilityOptions, setVisibilityOptions] = useState([]);
//     const [challengeStatusOptions, setChallengeStatusOptions] = useState([]);
//     const [challengeTypeOptions, setChallengeTypeOptions] = useState([]);

//     const [currentPage, setCurrentPage] = useState(0);
//     const [totalPages, setTotalPages] = useState(0);

//     const [filters, setFilters] = useState({
//         title: "",
//         description: "",
//         startTime: "",
//         endTime: "",
//         type: "",
//         status: "",
//         visibility: ""
//     });

//     const [form, setForm] = useState({
//         title: "",
//         description: "",
//         startTime: "",
//         endTime: "",
//         visibility: "PUBLIC",
//         status: "DRAFT",
//         rule: {
//             type: "TOTAL_DISTANCE",
//             targetValue: "",
//             durationDays: "",
//             dailyTarget: "",
//             minSpeed: ""
//         },
//         reward: {
//             money: "",
//             badge: ""
//         }
//     });

//     const [editingId, setEditingId] = useState(null);

//     const [selectedChallenge, setSelectedChallenge] = useState(null);

//     const [routeForm, setRouteForm] = useState({
//         polyline: "",
//         totalDistance: "",
//         requiredLoops: "",
//         checkpoints: []
//     });

//     const [editingRouteId, setEditingRouteId] = useState(null);
//     const [showRouteModal, setShowRouteModal] = useState(false);

//     const [newCheckpoint, setNewCheckpoint] = useState({
//         latitude: "",
//         longitude: "",
//         radius: "",
//         name: ""
//     });

//     const mapRef = useRef(null);
//     const mapInstanceRef = useRef(null);
//     const polylineRef = useRef(null);
//     const markersRef = useRef([]);
//     const mapCoordsRef = useRef([]);

//     const manageMapRef = useRef(null);
//     const manageMapInstanceRef = useRef(null);
//     const managePolylineRef = useRef(null);
//     const manageMarkersRef = useRef([]);

//     const showNotif = (type, message) => {
//         setNotification({ type, message });
//         setTimeout(() => setNotification({ type: "", message: "" }), 3000);
//     };

//     const formatDate = (value) => {
//         if (!value) return "";
//         try {
//             return new Date(value).toLocaleString("vi-VN");
//         } catch {
//             return value;
//         }
//     };

//     const fetchOptions = async () => {
//         try {
//             const [visRes, statusRes, typeRes] = await Promise.all([
//                 axiosClient.get("/api/options/visibility"),
//                 axiosClient.get("/api/options/challenge-status"),
//                 axiosClient.get("/api/options/challenge-type")
//             ]);

//             setVisibilityOptions(visRes.data || []);
//             setChallengeStatusOptions(statusRes.data || []);
//             setChallengeTypeOptions(typeRes.data || []);
//         } catch (error) {
//             console.error("Failed to load options", error);
//             showNotif("error", "Không lấy được danh sách option");
//         }
//     };

//     const buildFilterParams = () => {
//         const params = {
//             page: currentPage,
//             size: 10
//         };
//         if (filters.title) params.title = filters.title;
//         if (filters.description) params.description = filters.description;
//         if (filters.type) params.type = filters.type;
//         if (filters.status) params.status = filters.status;
//         if (filters.visibility) params.visibility = filters.visibility;
//         if (filters.startTime) params.startTime = new Date(filters.startTime).toISOString();
//         if (filters.endTime) params.endTime = new Date(filters.endTime).toISOString();
//         return params;
//     };

//     const fetchChallenges = async () => {
//         try {
//             setLoading(true);
//             const res = await axiosClient.get("/api/admin/challenges/filters", {
//                 params: buildFilterParams()
//             });
//             const pageData = res.data?.data;
//             setChallenges(pageData?.content || []);
//             setTotalPages(pageData?.totalPages || 0);
//         } catch (error) {
//             console.error("Failed fetch challenges", error);
//             showNotif("error", "Lấy danh sách challenge thất bại");
//         } finally {
//             setLoading(false);
//         }
//     };

//     const fetchChallenge = async (id) => {
//         try {
//             const res = await axiosClient.get(`/api/admin/challenges/${id}`);
//             setSelectedChallenge(res.data?.data || null);
//         } catch (error) {
//             console.error("Failed fetch challenge detail", error);
//             showNotif("error", "Lấy challenge thất bại");
//         }
//     };

//     const resetForm = () => {
//         setForm({
//             title: "",
//             description: "",
//             startTime: "",
//             endTime: "",
//             visibility: "PUBLIC",
//             status: "DRAFT",
//             rule: {
//                 type: "TOTAL_DISTANCE",
//                 targetValue: "",
//                 durationDays: "",
//                 dailyTarget: "",
//                 minSpeed: ""
//             },
//             reward: {
//                 money: "",
//                 badge: ""
//             }
//         });
//         setEditingId(null);
//         setSelectedChallenge(null);
//     };

//     const onEditChallenge = (challenge) => {
//         setForm({
//             title: challenge.title || "",
//             description: challenge.description || "",
//             startTime: challenge.startTime ? new Date(challenge.startTime).toISOString().slice(0, 16) : "",
//             endTime: challenge.endTime ? new Date(challenge.endTime).toISOString().slice(0, 16) : "",
//             visibility: challenge.visibility || "PUBLIC",
//             status: challenge.status || "DRAFT",
//             rule: {
//                 type: challenge.rule?.type || "TOTAL_DISTANCE",
//                 targetValue: challenge.rule?.targetValue ?? "",
//                 durationDays: challenge.rule?.durationDays ?? "",
//                 dailyTarget: challenge.rule?.dailyTarget ?? "",
//                 minSpeed: challenge.rule?.minSpeed ?? ""
//             },
//             reward: {
//                 money: challenge.reward?.money ?? "",
//                 badge: challenge.reward?.badge || ""
//             }
//         });
//         setEditingId(challenge.id);
//         setActiveTab("create");
//     };

//     const buildCreateUpdatePayload = () => {
//         return {
//             title: form.title,
//             description: form.description,
//             startTime: new Date(form.startTime).toISOString(),
//             endTime: new Date(form.endTime).toISOString(),
//             visibility: form.visibility,
//             status: form.status,
//             rule: {
//                 type: form.rule.type,
//                 targetValue: form.rule.targetValue ? Number(form.rule.targetValue) : 0,
//                 durationDays: form.rule.durationDays ? Number(form.rule.durationDays) : 0,
//                 dailyTarget: form.rule.dailyTarget ? Number(form.rule.dailyTarget) : 0,
//                 minSpeed: form.rule.minSpeed ? Number(form.rule.minSpeed) : 0
//             },
//             reward: {
//                 money: form.reward.money ? Number(form.reward.money) : 0,
//                 badge: form.reward.badge || ""
//             }
//         };
//     };

//     const handleCreateUpdate = async () => {
//         try {
//             if (!form.title || form.title.trim().length < 3) {
//                 showNotif("error", "Tiêu đề phải có ít nhất 3 ký tự");
//                 return;
//             }
//             if (!form.description || form.description.trim().length < 5) {
//                 showNotif("error", "Mô tả không được để trống");
//                 return;
//             }
//             if (!form.startTime || !form.endTime) {
//                 showNotif("error", "Phải chọn thời gian bắt đầu và kết thúc");
//                 return;
//             }
//             if (new Date(form.startTime) >= new Date(form.endTime)) {
//                 showNotif("error", "Thời gian kết thúc phải sau thời gian bắt đầu");
//                 return;
//             }
//             if (!form.reward.badge || form.reward.badge.trim().length === 0) {
//                 showNotif("error", "Badge reward không được để trống");
//                 return;
//             }

//             const payload = buildCreateUpdatePayload();

//             if (editingId) {
//                 await axiosClient.put(`/api/admin/challenges/${editingId}`, payload);
//                 showNotif("success", "Cập nhật challenge thành công");
//             } else {
//                 await axiosClient.post("/api/admin/challenges", payload);
//                 showNotif("success", "Tạo challenge thành công");
//             }

//             resetForm();
//             setActiveTab("list");
//             setCurrentPage(0);
//             fetchChallenges();
//         } catch (error) {
//             console.error("Create/Update error", error);
//             showNotif("error", error.response?.data?.message || "Lỗi khi lưu challenge");
//         }
//     };

//     const handlePublish = async (id) => {
//         try {
//             await axiosClient.patch(`/api/admin/challenges/${id}/publish`);
//             showNotif("success", "Publish thành công");
//             fetchChallenges();
//         } catch (error) {
//             console.error("Publish error", error);
//             showNotif("error", error.response?.data?.message || "Lỗi publish");
//         }
//     };

//     const handleDelete = async (id) => {
//         if (!window.confirm("Bạn chắc chứ?")) return;
//         try {
//             await axiosClient.delete(`/api/admin/challenges/${id}`);
//             showNotif("success", "Xóa thành công");
//             fetchChallenges();
//         } catch (error) {
//             console.error("Delete error", error);
//             showNotif("error", error.response?.data?.message || "Xóa thất bại");
//         }
//     };

//     const initializeEditorMap = () => {
//         if (!mapRef.current || mapInstanceRef.current) return;
//         mapInstanceRef.current = L.map(mapRef.current).setView([21.0285, 105.8542], 13);
//         L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", { maxZoom: 19, attribution: "&copy; OpenStreetMap contributors" }).addTo(mapInstanceRef.current);

//         mapInstanceRef.current.on("click", (e) => {
//             const { lat, lng } = e.latlng;
//             mapCoordsRef.current.push([lat, lng]);
//             const marker = L.marker([lat, lng]).bindPopup(`Point ${mapCoordsRef.current.length}`).addTo(mapInstanceRef.current);
//             markersRef.current.push(marker);

//             if (mapCoordsRef.current.length >= 2) {
//                 if (polylineRef.current) mapInstanceRef.current.removeLayer(polylineRef.current);
//                 polylineRef.current = L.polyline(mapCoordsRef.current, { color: "blue", weight: 3 }).addTo(mapInstanceRef.current);
//                 const dist = calculateDistance(mapCoordsRef.current);
//                 setRouteForm(prev => ({ ...prev, polyline: JSON.stringify(mapCoordsRef.current), totalDistance: dist.toFixed(0) }));
//             }
//         });
//     };

//     const calculateDistance = (coords) => {
//         const R = 6371000;
//         let total = 0;
//         for (let i = 0; i < coords.length - 1; i++) {
//             const [lat1, lon1] = coords[i];
//             const [lat2, lon2] = coords[i + 1];
//             const dlat = (lat2 - lat1) * (Math.PI / 180);
//             const dlon = (lon2 - lon1) * (Math.PI / 180);
//             const a = Math.sin(dlat / 2) ** 2 + Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) * Math.sin(dlon / 2) ** 2;
//             const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//             total += R * c;
//         }
//         return total;
//     };

//     const clearEditorMap = () => {
//         if (mapInstanceRef.current) {
//             if (polylineRef.current) mapInstanceRef.current.removeLayer(polylineRef.current);
//             markersRef.current.forEach(m => mapInstanceRef.current.removeLayer(m));
//             polylineRef.current = null;
//             markersRef.current = [];
//         }
//         mapCoordsRef.current = [];
//         setRouteForm(prev => ({ ...prev, polyline: "", totalDistance: "" }));
//     };

//     const initializeManageMap = () => {
//         if (!manageMapRef.current) return;

//         if (manageMapInstanceRef.current) {
//             manageMapInstanceRef.current.remove();
//             manageMapInstanceRef.current = null;
//         }

//         manageMapInstanceRef.current = L.map(manageMapRef.current).setView([21.0285, 105.8542], 5);
//         L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", { maxZoom: 19, attribution: "&copy; OpenStreetMap contributors" }).addTo(manageMapInstanceRef.current);
//     };

//     const drawManageRoute = (route) => {
//         if (!manageMapInstanceRef.current) return;

//         if (managePolylineRef.current) {
//             manageMapInstanceRef.current.removeLayer(managePolylineRef.current);
//             managePolylineRef.current = null;
//         }
//         manageMarkersRef.current.forEach(m => manageMapInstanceRef.current.removeLayer(m));
//         manageMarkersRef.current = [];

//         if (!route?.polyline) return;

//         let coords;
//         try {
//             coords = typeof route.polyline === "string" ? JSON.parse(route.polyline) : route.polyline;
//         } catch {
//             coords = [];
//         }

//         if (!Array.isArray(coords) || coords.length === 0) return;

//         const latlngs = coords.map(pt => Array.isArray(pt) ? [Number(pt[0]), Number(pt[1])] : [Number(pt.latitude), Number(pt.longitude)]);

//         if (latlngs.length > 0) {
//             managePolylineRef.current = L.polyline(latlngs, { color: "blue", weight: 3 }).addTo(manageMapInstanceRef.current);
//             manageMapInstanceRef.current.fitBounds(managePolylineRef.current.getBounds(), { padding: [20, 20] });
//         }

//         (route.checkpoint || []).forEach(cp => {
//             if (cp.latitude != null && cp.longitude != null) {
//                 const marker = L.marker([Number(cp.latitude), Number(cp.longitude)]).bindPopup(`${cp.name || "Checkpoint"}${cp.radius ? ` (${cp.radius}m)` : ""}`).addTo(manageMapInstanceRef.current);
//                 manageMarkersRef.current.push(marker);
//                 if (cp.radius) {
//                     const circle = L.circle([Number(cp.latitude), Number(cp.longitude)], { radius: Number(cp.radius), color: "red", fillOpacity: 0.08 }).addTo(manageMapInstanceRef.current);
//                     manageMarkersRef.current.push(circle);
//                 }
//             }
//         });
//     };

//     const clearManageMap = () => {
//         if (manageMapInstanceRef.current) {
//             if (managePolylineRef.current) manageMapInstanceRef.current.removeLayer(managePolylineRef.current);
//             manageMarkersRef.current.forEach(m => manageMapInstanceRef.current.removeLayer(m));
//             managePolylineRef.current = null;
//             manageMarkersRef.current = [];
//         }
//     };

//     const resetRouteForm = () => {
//         setRouteForm({ polyline: "", totalDistance: "", requiredLoops: "", checkpoints: [] });
//         setNewCheckpoint({ latitude: "", longitude: "", radius: "", name: "" });
//         setEditingRouteId(null);
//         clearEditorMap();
//     };

//     const onAddCheckpoint = () => {
//         if (!newCheckpoint.latitude || !newCheckpoint.longitude || !newCheckpoint.name) {
//             showNotif("error", "Latitude, longitude, name bắt buộc");
//             return;
//         }
//         setRouteForm(prev => ({
//             ...prev,
//             checkpoints: [...prev.checkpoints, {
//                 latitude: Number(newCheckpoint.latitude),
//                 longitude: Number(newCheckpoint.longitude),
//                 radius: newCheckpoint.radius ? Number(newCheckpoint.radius) : 0,
//                 orderIndex: prev.checkpoints.length,
//                 name: newCheckpoint.name
//             }]
//         }));
//         setNewCheckpoint({ latitude: "", longitude: "", radius: "", name: "" });
//     };

//     const onRemoveCheckpoint = (idx) => {
//         setRouteForm(prev => ({ ...prev, checkpoints: prev.checkpoints.filter((_, i) => i !== idx) }));
//     };

//     const handleRouteSave = async () => {
//         try {
//             if (!routeForm.polyline || !routeForm.totalDistance || !routeForm.requiredLoops) {
//                 showNotif("error", "Polyline, totalDistance, requiredLoops là bắt buộc");
//                 return;
//             }
//             if (routeForm.checkpoints.length === 0) {
//                 showNotif("error", "Cần ít nhất 1 checkpoint");
//                 return;
//             }

//             const payload = {
//                 polyline: routeForm.polyline,
//                 totalDistance: Number(routeForm.totalDistance),
//                 requiredLoops: Number(routeForm.requiredLoops),
//                 checkpoints: routeForm.checkpoints
//             };

//             if (!selectedChallenge?.id) {
//                 showNotif("error", "Challenge chưa được chọn");
//                 return;
//             }

//             if (editingRouteId) {
//                 await axiosClient.put(`/api/admin/challenges/${selectedChallenge.id}/routes/${editingRouteId}`, payload);
//                 showNotif("success", "Cập nhật route thành công");
//             } else {
//                 await axiosClient.post(`/api/admin/challenges/${selectedChallenge.id}/routes`, payload);
//                 showNotif("success", "Tạo route thành công");
//             }

//             setShowRouteModal(false);
//             resetRouteForm();
//             await fetchChallenge(selectedChallenge.id);
//             await fetchChallenges();
//         } catch (error) {
//             console.error("Save route error", error);
//             showNotif("error", error.response?.data?.message || "Lỗi lưu route");
//         }
//     };

//     const onRouteEdit = () => {
//         if (!selectedChallenge?.route) return;
//         const r = selectedChallenge.route;
//         let coords = [];
//         try { coords = typeof r.polyline === "string" ? JSON.parse(r.polyline) : r.polyline; } catch { coords = []; }

//         setRouteForm({
//             polyline: r.polyline || "",
//             totalDistance: r.totalDistance || "",
//             requiredLoops: r.requiredLoops || "",
//             checkpoints: r.checkpoint || []
//         });
//         setEditingRouteId(r.id);
//         setShowRouteModal(true);

//         if (mapInstanceRef.current) {
//             mapInstanceRef.current.remove();
//             mapInstanceRef.current = null;
//         }
//         initializeEditorMap();

//         if (coords.length > 0) {
//             coords.forEach((p, idx) => {
//                 const [lat, lng] = Array.isArray(p) ? [Number(p[0]), Number(p[1])] : [Number(p.latitude), Number(p.longitude)];
//                 mapCoordsRef.current.push([lat, lng]);
//                 const marker = L.marker([lat, lng]).addTo(mapInstanceRef.current);
//                 markersRef.current.push(marker);
//             });
//             polylineRef.current = L.polyline(coords, { color: "blue", weight: 3 }).addTo(mapInstanceRef.current);
//         }
//     };

//     useEffect(() => { fetchOptions(); }, []);
//     useEffect(() => { fetchChallenges(); }, [currentPage, filters]);

//     useEffect(() => {
//         if (activeTab === "manageRoutes") {
//             initializeManageMap();
//             if (selectedChallenge?.route) drawManageRoute(selectedChallenge.route);
//         } else {
//             clearManageMap();
//         }
//     }, [activeTab, selectedChallenge]);

//     return (
//         <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
//             {notification.message && (
//                 <div style={{ padding: "10px 15px", marginBottom: "20px", backgroundColor: notification.type === "success" ? "#4caf50" : "#f44336", color: "white", borderRadius: "4px" }}>
//                     {notification.message}
//                 </div>
//             )}

//             <div style={{ marginBottom: "20px", display: "flex", gap: "10px" }}>
//                 <button onClick={() => { setActiveTab("list"); setCurrentPage(0); }} style={{ padding: "10px 20px", backgroundColor: activeTab === "list" ? "#2196F3" : "#ddd", color: activeTab === "list" ? "white" : "black", border: "none", cursor: "pointer", borderRadius: "4px" }}>
//                     Challenge List
//                 </button>
//                 <button onClick={() => { resetForm(); setActiveTab("create"); }} style={{ padding: "10px 20px", backgroundColor: activeTab === "create" ? "#2196F3" : "#ddd", color: activeTab === "create" ? "white" : "black", border: "none", cursor: "pointer", borderRadius: "4px" }}>
//                     {editingId ? "Edit Challenge" : "Create Challenge"}
//                 </button>
//                 {selectedChallenge && selectedChallenge.rule?.type === "CHECKPOINT" && (
//                     <button onClick={() => { setActiveTab("manageRoutes"); fetchChallenge(selectedChallenge.id); }} style={{ padding: "10px 20px", backgroundColor: activeTab === "manageRoutes" ? "#2196F3" : "#ddd", color: activeTab === "manageRoutes" ? "white" : "black", border: "none", cursor: "pointer", borderRadius: "4px" }}>
//                         Manage Routes
//                     </button>
//                 )}
//             </div>

//             {activeTab === "list" && (
//                 <div>
//                     <h2>Challenge List</h2>
//                     <div style={{ marginBottom: "20px", display: "flex", gap: "10px", flexWrap: "wrap" }}>
//                         <input placeholder="Title" value={filters.title} onChange={e => { setFilters(f => ({ ...f, title: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                         <input placeholder="Description" value={filters.description} onChange={e => { setFilters(f => ({ ...f, description: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                         <input type="date" value={filters.startTime} onChange={e => { setFilters(f => ({ ...f, startTime: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                         <input type="date" value={filters.endTime} onChange={e => { setFilters(f => ({ ...f, endTime: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                         <select value={filters.type} onChange={e => { setFilters(f => ({ ...f, type: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}>
//                             <option value="">All type</option>
//                             {challengeTypeOptions.map(v => <option key={v} value={v}>{v}</option>)}
//                         </select>
//                         <select value={filters.status} onChange={e => { setFilters(f => ({ ...f, status: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}>
//                             <option value="">All status</option>
//                             {challengeStatusOptions.map(v => <option key={v} value={v}>{v}</option>)}
//                         </select>
//                         <select value={filters.visibility} onChange={e => { setFilters(f => ({ ...f, visibility: e.target.value })); setCurrentPage(0); }} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}>
//                             <option value="">All visibility</option>
//                             {visibilityOptions.map(v => <option key={v} value={v}>{v}</option>)}
//                         </select>
//                     </div>

//                     {loading ? <p>Loading...</p> : (
//                         <table style={{ width: "100%", borderCollapse: "collapse" }}>
//                             <thead><tr style={{ backgroundColor: "#f5f5f5" }}>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>ID</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>Title</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>Type</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>Visibility</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>Status</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>StartTime</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>EndTime</th>
//                                 <th style={{ border: "1px solid #ddd", padding: "10px" }}>Actions</th>
//                             </tr></thead>
//                             <tbody>
//                                 {challenges.map(c => (
//                                     <tr key={c.id} style={{ borderBottom: "1px solid #ddd" }}>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{c.id}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{c.title}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{c.rule?.type}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{c.visibility}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{c.status}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{formatDate(c.startTime)}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px" }}>{formatDate(c.endTime)}</td>
//                                         <td style={{ border: "1px solid #ddd", padding: "10px", textAlign: "center" }}>
//                                             <button onClick={() => onEditChallenge(c)} style={{ margin: "2px", padding: "5px 8px", backgroundColor: "#2196F3", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Edit</button>
//                                             {c.status === "DRAFT" && <button onClick={() => handlePublish(c.id)} style={{ margin: "2px", padding: "5px 8px", backgroundColor: "#4caf50", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Publish</button>}
//                                             {c.rule?.type === "CHECKPOINT" && <button onClick={() => { setSelectedChallenge(c); setActiveTab("manageRoutes"); fetchChallenge(c.id); }} style={{ margin: "2px", padding: "5px 8px", backgroundColor: "#ff9800", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Route</button>}
//                                             <button onClick={() => handleDelete(c.id)} style={{ margin: "2px", padding: "5px 8px", backgroundColor: "#f44336", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Delete</button>
//                                         </td>
//                                     </tr>
//                                 ))}
//                             </tbody>
//                         </table>
//                     )}

//                     <div style={{ marginTop: "20px", display: "flex", gap: "10px", alignItems: "center" }}>
//                         <button disabled={currentPage <= 0} onClick={() => setCurrentPage(p => Math.max(p - 1, 0))} style={{ padding: "8px 15px", backgroundColor: currentPage <= 0 ? "#ccc" : "#2196F3", color: "white", border: "none", borderRadius: "4px", cursor: currentPage <= 0 ? "not-allowed" : "pointer" }}>Previous</button>
//                         <span>Page {currentPage + 1} / {totalPages}</span>
//                         <button disabled={currentPage >= totalPages - 1} onClick={() => setCurrentPage(p => Math.min(p + 1, (totalPages || 1) - 1))} style={{ padding: "8px 15px", backgroundColor: currentPage >= totalPages - 1 ? "#ccc" : "#2196F3", color: "white", border: "none", borderRadius: "4px", cursor: currentPage >= totalPages - 1 ? "not-allowed" : "pointer" }}>Next</button>
//                     </div>
//                 </div>
//             )}

//             {activeTab === "create" && (
//                 <div>
//                     <h2>{editingId ? "Sửa Challenge" : "Tạo Challenge"}</h2>
//                     <div style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)", gap: "15px", maxWidth: "1200px" }}>
//                         <div><label>Title *</label><input value={form.title} onChange={e => setForm(prev => ({ ...prev, title: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Description *</label><textarea value={form.description} onChange={e => setForm(prev => ({ ...prev, description: e.target.value }))} style={{ width: "100%", minHeight: "60px", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Start Time *</label><input type="datetime-local" value={form.startTime} onChange={e => setForm(prev => ({ ...prev, startTime: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>End Time *</label><input type="datetime-local" value={form.endTime} onChange={e => setForm(prev => ({ ...prev, endTime: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Visibility *</label><select value={form.visibility} onChange={e => setForm(prev => ({ ...prev, visibility: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}><option value="">Select visibility</option>{visibilityOptions.map(v => <option key={v} value={v}>{v}</option>)}</select></div>
//                         <div><label>Status *</label><select value={form.status} onChange={e => setForm(prev => ({ ...prev, status: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}><option value="">Select status</option>{challengeStatusOptions.map(s => <option key={s} value={s}>{s}</option>)}</select></div>

//                         <h3 style={{ gridColumn: "1 / -1", marginTop: "10px" }}>Rule</h3>
//                         <div><label>Type *</label><select value={form.rule.type} onChange={e => setForm(prev => ({ ...prev, rule: { ...prev.rule, type: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}><option value="">Select rule type</option>{challengeTypeOptions.map(t => <option key={t} value={t}>{t}</option>)}</select></div>
//                         <div><label>Target Value</label><input type="number" value={form.rule.targetValue} onChange={e => setForm(prev => ({ ...prev, rule: { ...prev.rule, targetValue: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Duration Days</label><input type="number" value={form.rule.durationDays} onChange={e => setForm(prev => ({ ...prev, rule: { ...prev.rule, durationDays: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Daily Target</label><input type="number" value={form.rule.dailyTarget} onChange={e => setForm(prev => ({ ...prev, rule: { ...prev.rule, dailyTarget: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Min Speed</label><input type="number" value={form.rule.minSpeed} onChange={e => setForm(prev => ({ ...prev, rule: { ...prev.rule, minSpeed: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>

//                         <h3 style={{ gridColumn: "1 / -1", marginTop: "10px" }}>Reward</h3>
//                         <div><label>Money</label><input type="number" value={form.reward.money} onChange={e => setForm(prev => ({ ...prev, reward: { ...prev.reward, money: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         <div><label>Badge *</label><input value={form.reward.badge} onChange={e => setForm(prev => ({ ...prev, reward: { ...prev.reward, badge: e.target.value } }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                     </div>

//                     <div style={{ marginTop: "20px", display: "flex", gap: "10px" }}>
//                         <button onClick={handleCreateUpdate} style={{ padding: "10px 20px", backgroundColor: "#4caf50", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>{editingId ? "Update Challenge" : "Create Challenge"}</button>
//                         <button onClick={() => { setActiveTab("list"); resetForm(); }} style={{ padding: "10px 20px", backgroundColor: "#9e9e9e", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Cancel</button>
//                     </div>
//                 </div>
//             )}

//             {activeTab === "manageRoutes" && selectedChallenge && (
//                 <div>
//                     <h2>Manage Route for: {selectedChallenge.title}</h2>
//                     {selectedChallenge.route ? (
//                         <div style={{ marginBottom: "20px" }}>
//                             <div style={{ backgroundColor: "#f5f5f5", padding: "15px", borderRadius: "4px" }}>
//                                 <p><strong>Total Distance:</strong> {(selectedChallenge.route.totalDistance / 1000).toFixed(2)} km</p>
//                                 <p><strong>Required Loops:</strong> {selectedChallenge.route.requiredLoops}</p>
//                                 <p><strong>Checkpoints:</strong> {selectedChallenge.route.checkpoint?.length || 0}</p>
//                             </div>
//                             <button onClick={() => { onRouteEdit(); }} style={{ margin: "10px 0", padding: "10px 20px", backgroundColor: "#2196F3", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Edit Route</button>
//                         </div>
//                     ) : (
//                         <div style={{ marginBottom: "20px" }}>
//                             <p>Chưa có route cho challenge này.</p>
//                             <button onClick={() => { setShowRouteModal(true); resetRouteForm(); initializeEditorMap(); }} style={{ padding: "10px 20px", backgroundColor: "#4caf50", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Create Route</button>
//                         </div>
//                     )}

//                     <div>
//                         <h3>Route Map Preview</h3>
//                         <div style={{ width: "100%", height: "400px", border: "1px solid #ccc", borderRadius: "4px" }}><div ref={manageMapRef} style={{ width: "100%", height: "100%" }} /></div>
//                     </div>
//                 </div>
//             )}

//             {showRouteModal && selectedChallenge && (
//                 <div style={{ position: "fixed", top: 0, left: 0, right: 0, bottom: 0, backgroundColor: "rgba(0,0,0,0.5)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000 }}>
//                     <div style={{ backgroundColor: "white", borderRadius: "8px", padding: "20px", maxWidth: "960px", width: "95%", maxHeight: "90vh", overflowY: "auto" }}>
//                         <h2>{editingRouteId ? "Edit Route" : "Create Route"}</h2>
//                         <div style={{ width: "100%", height: "350px", border: "1px solid #ccc", borderRadius: "4px", marginBottom: "15px" }}><div ref={mapRef} style={{ width: "100%", height: "100%" }} /></div>
//                         <p style={{ fontSize: "12px", color: "#666" }}>* Click vào bản đồ để vẽ đường chạy</p>

//                         <div style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)", gap: "15px", marginBottom: "15px" }}>
//                             <div><label>Total Distance (m)</label><input type="number" value={routeForm.totalDistance} readOnly style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc", backgroundColor: "#f5f5f5" }} /></div>
//                             <div><label>Required Loops *</label><input type="number" value={routeForm.requiredLoops} onChange={e => setRouteForm(prev => ({ ...prev, requiredLoops: e.target.value }))} style={{ width: "100%", padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} /></div>
//                         </div>

//                         <h3>Checkpoints</h3>
//                         <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: "10px", marginBottom: "10px" }}>
//                             <input type="number" placeholder="Latitude" value={newCheckpoint.latitude} onChange={e => setNewCheckpoint(prev => ({ ...prev, latitude: e.target.value }))} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                             <input type="number" placeholder="Longitude" value={newCheckpoint.longitude} onChange={e => setNewCheckpoint(prev => ({ ...prev, longitude: e.target.value }))} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                             <input placeholder="Name" value={newCheckpoint.name} onChange={e => setNewCheckpoint(prev => ({ ...prev, name: e.target.value }))} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                             <input type="number" placeholder="Radius (m)" value={newCheckpoint.radius} onChange={e => setNewCheckpoint(prev => ({ ...prev, radius: e.target.value }))} style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }} />
//                         </div>
//                         <button onClick={onAddCheckpoint} style={{ marginBottom: "15px", padding: "8px 12px", backgroundColor: "#4caf50", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Add Checkpoint</button>

//                         {routeForm.checkpoints.length > 0 && routeForm.checkpoints.map((cp, idx) => (
//                             <div key={idx} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "6px", border: "1px solid #ccc", borderRadius: "4px", padding: "6px" }}>
//                                 <span>{cp.name} - {cp.latitude}, {cp.longitude} {cp.radius ? `(${cp.radius}m)` : ""}</span>
//                                 <button onClick={() => onRemoveCheckpoint(idx)} style={{ padding: "4px 8px", backgroundColor: "#ff9800", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Remove</button>
//                             </div>
//                         ))}

//                         <div style={{ display: "flex", gap: "10px", marginTop: "15px" }}>
//                             <button onClick={handleRouteSave} style={{ padding: "10px 20px", backgroundColor: "#4caf50", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>{editingRouteId ? "Update Route" : "Save Route"}</button>
//                             <button onClick={clearEditorMap} style={{ padding: "10px 20px", backgroundColor: "#f57c00", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Clear Map</button>
//                             <button onClick={() => { setShowRouteModal(false); resetRouteForm(); }} style={{ padding: "10px 20px", backgroundColor: "#9e9e9e", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Cancel</button>
//                         </div>
//                     </div>
//                 </div>
//             )}
//         </div>
//     );
// };

// export default ChallengePage;