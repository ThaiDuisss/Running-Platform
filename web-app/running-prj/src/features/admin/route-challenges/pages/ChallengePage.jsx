import { useEffect, useState } from "react";
import axiosClient from "@/shared/services/axiosClient";

const ChallengePage = () => {

    const [challenges, setChallenges] = useState([]);
    const [loading, setLoading] = useState(false);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const [statusOptions, setStatusOptions] = useState([]);
    const [visibilityOptions, setVisibilityOptions] = useState([]);
    const [typeOptions, setTypeOptions] = useState([]);

    // ================= FILTER =================
    const [filters, setFilters] = useState({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        visibility: "",
        status: "",
        type: ""
    });

    // ================= CREATE FORM =================
    const [form, setForm] = useState({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        visibility: "",

        // rule
        type: "",
        targetValue: "",
        durationDays: "",
        dailyTarget: "",
        minSpeed: "",

        // reward
        money: "",
        badge: ""
    });

    const formatDate = (date) => {
        if (!date) return "";
        return new Date(date).toLocaleString("vi-VN");
    };

    // ================= LOAD OPTIONS =================
    const fetchOptions = async () => {
        try {
            const [statusRes, visibilityRes, typeRes] = await Promise.all([
                axiosClient.get("/api/options/challenge-status"),
                axiosClient.get("/api/options/visibility"),
                axiosClient.get("/api/options/challenge-type")
            ]);

            setStatusOptions(statusRes.data || []);
            setVisibilityOptions(visibilityRes.data || []);
            setTypeOptions(typeRes.data || []);
        } catch (err) {
            console.error(err);
        }
    };

    // ================= FETCH LIST =================
    const fetchChallenges = async () => {
        try {
            setLoading(true);

            const res = await axiosClient.get("/api/admin/challenges/filters", {
                params: {
                    page: currentPage,
                    size: 10,
                    ...filters
                }
            });

            const data = res.data.data;

            setChallenges(data?.content || []);
            setTotalPages(data?.totalPages || 0);

        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    // ================= CREATE =================
    const handleCreate = async () => {
        try {

            const payload = {
                title: form.title,
                description: form.description,
                startTime: new Date(form.startTime).toISOString(),
                endTime: new Date(form.endTime).toISOString(),
                visibility: form.visibility,

                rule: {
                    type: form.type,
                    targetValue: Number(form.targetValue),
                    durationDays: Number(form.durationDays),
                    dailyTarget: Number(form.dailyTarget),
                    minSpeed: Number(form.minSpeed)
                },

                reward: {
                    money: Number(form.money),
                    badge: form.badge
                }
            };

            await axiosClient.post("/api/admin/challenges", payload);

            alert("Create success");
            fetchChallenges();

        } catch (e) {
            console.error(e);
            alert("Create failed");
        }
    };

    // ================= PUBLISH =================
    const handlePublish = async (id) => {
        try {
            await axiosClient.patch(`/api/admin/challenges/${id}/publish`);
            fetchChallenges();
        } catch (e) {
            alert("Publish failed");
        }
    };

    // ================= DELETE =================
    const handleDelete = async (id) => {
        try {
            await axiosClient.delete(`/api/admin/challenges/${id}`);
            fetchChallenges();
        } catch (e) {
            alert("Delete failed");
        }
    };

    useEffect(() => {
        fetchOptions();
    }, []);

    useEffect(() => {
        fetchChallenges();
    }, [currentPage]);

    return (
        <div style={{ padding: 20 }}>

            <h2>Create Challenge</h2>

            {/* ===== CREATE FORM ===== */}
            <div style={{ display: "flex", flexWrap: "wrap", gap: 10, marginBottom: 20 }}>

                <input placeholder="Title" onChange={e => setForm({ ...form, title: e.target.value })} />
                <input placeholder="Description" onChange={e => setForm({ ...form, description: e.target.value })} />

                <input type="datetime-local" onChange={e => setForm({ ...form, startTime: e.target.value })} />
                <input type="datetime-local" onChange={e => setForm({ ...form, endTime: e.target.value })} />

                <select onChange={e => setForm({ ...form, visibility: e.target.value })}>
                    <option value="">Visibility</option>
                    {visibilityOptions.map(v => <option key={v}>{v}</option>)}
                </select>

                {/* RULE */}
                <select onChange={e => setForm({ ...form, type: e.target.value })}>
                    <option value="">Type</option>
                    {typeOptions.map(t => <option key={t}>{t}</option>)}
                </select>

                <input placeholder="Target Value" type="number"
                    onChange={e => setForm({ ...form, targetValue: e.target.value })} />

                <input placeholder="Duration Days" type="number"
                    onChange={e => setForm({ ...form, durationDays: e.target.value })} />

                <input placeholder="Daily Target" type="number"
                    onChange={e => setForm({ ...form, dailyTarget: e.target.value })} />

                <input placeholder="Min Speed" type="number"
                    onChange={e => setForm({ ...form, minSpeed: e.target.value })} />

                {/* REWARD */}
                <input placeholder="Money" type="number"
                    onChange={e => setForm({ ...form, money: e.target.value })} />

                <input placeholder="Badge"
                    onChange={e => setForm({ ...form, badge: e.target.value })} />

                <button onClick={handleCreate}>Create</button>
            </div>

            <h2>Challenge List</h2>

            {/* ===== FILTER ===== */}
            <div style={{ marginBottom: 20, display: "flex", flexWrap: "wrap", gap: 10 }}>
                <input placeholder="Title"
                    onChange={e => setFilters({ ...filters, title: e.target.value })} />

                <select onChange={e => setFilters({ ...filters, type: e.target.value })}>
                    <option value="">Type</option>
                    {typeOptions.map(t => <option key={t}>{t}</option>)}
                </select>

                <button onClick={() => {
                    setCurrentPage(0);
                    fetchChallenges();
                }}>
                    Filter
                </button>
            </div>

            {/* ===== TABLE ===== */}
            {loading ? <p>Loading...</p> : (
                <table border="1" cellPadding="10" style={{ width: "100%" }}>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Status</th>
                            <th>Type</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>
                        {challenges.map(c => (
                            <tr key={c.id}>
                                <td>{c.id}</td>
                                <td>{c.title}</td>
                                <td>{c.status}</td>
                                <td>{c.rule?.type}</td>
                                <td>
                                    <button onClick={() => handlePublish(c.id)}>Publish</button>
                                    <button onClick={() => handleDelete(c.id)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

            {/* ===== PAGINATION ===== */}
            <div style={{ marginTop: 20 }}>
                <button disabled={currentPage === 0}
                    onClick={() => setCurrentPage(p => p - 1)}>
                    Prev
                </button>

                <span> {currentPage + 1} / {totalPages} </span>

                <button disabled={currentPage === totalPages - 1}
                    onClick={() => setCurrentPage(p => p + 1)}>
                    Next
                </button>
            </div>

        </div>
    );
};

export default ChallengePage;