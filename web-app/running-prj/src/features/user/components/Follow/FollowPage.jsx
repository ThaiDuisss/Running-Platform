import React, { startTransition, useEffect, useMemo, useState } from "react";
import { Alert, Button, Container } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { MapPinned, Sparkles, Users } from "lucide-react";
import FollowSearchBar from "./components/FollowSearchBar";
import FollowRadiusFilter from "./components/FollowRadiusFilter";
import FollowTabs from "./components/FollowTabs";
import FollowUserCard from "./components/FollowUserCard";
import FollowUserSkeleton from "./components/FollowUserSkeleton";
import FollowEmptyState from "./components/FollowEmptyState";
import { followService } from "./services/followService";
import PaginationCustom from "@/features/admin/users/components/Pagination";
import "./follow.css";

const RADIUS_OPTIONS = [
    { label: "Tất cả", value: null },
    { label: "1 km", value: 1 },
    { label: "5 km", value: 5 },
    { label: "10 km", value: 10 },
    { label: "20 km", value: 20 },
    { label: "50 km", value: 50 },
];

const TAB_EMPTY_STATE = { discover: "discover", following: "following", followers: "followers" };

const FollowPage = () => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState("discover");
    const [rawKeyword, setRawKeyword] = useState("");
    const [keyword, setKeyword] = useState("");
    const [radiusKm, setRadiusKm] = useState(null);
    const [items, setItems] = useState([]);
    const [counts, setCounts] = useState({ discover: 0, following: 0, followers: 0 });
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(8);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [notice, setNotice] = useState("");
    const [pendingUserIds, setPendingUserIds] = useState([]);
    const [refreshKey, setRefreshKey] = useState(0);

    useEffect(() => {
        const timer = setTimeout(() => setKeyword(rawKeyword.trim()), 280);
        return () => clearTimeout(timer);
    }, [rawKeyword]);

    const loadFollowNetwork = async () => {
        try {
            setLoading(true);
            setError("");
            const response = await followService.getFollowNetwork({
                tab: activeTab,
                keyword,
                radiusKm,
                page: currentPage,
                size: pageSize,
            });
            setItems(response.items);
            setCounts(response.counts);
            setTotalPages(response.page?.totalPages || 0);
            setTotalElements(response.page?.totalElements || 0);
        } catch (loadError) {
            setError(loadError.message || "Không thể tải dữ liệu follow.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadFollowNetwork();
    }, [activeTab, keyword, radiusKm, refreshKey, currentPage, pageSize]);

    useEffect(() => {
        setCurrentPage(0);
    }, [activeTab, keyword, radiusKm]);

    const visibleStat = useMemo(() => ([
        { icon: Sparkles, label: "Gợi ý phù hợp", value: counts.discover },
        { icon: Users, label: "Đang follow", value: counts.following },
        { icon: MapPinned, label: radiusKm ? `Trong ${radiusKm} km` : "Không giới hạn bán kính", value: items.length },
    ]), [counts.discover, counts.following, items.length, radiusKm]);

    const handleFollowToggle = async (user) => {
        setPendingUserIds((prev) => [...prev, user.id]);
        setNotice("");
        try {
            await followService.toggleFollow(user.id, user.isFollowing);
            setRefreshKey((prev) => prev + 1);
            setNotice(user.isFollowing ? `Đã unfollow ${user.fullName}.` : `Bạn đang follow ${user.fullName}.`);
        } catch (toggleError) {
            setError(toggleError.message || "Không thể cập nhật follow.");
        } finally {
            setPendingUserIds((prev) => prev.filter((id) => id !== user.id));
        }
    };

    const handleMessage = (user) => {
        setNotice(`Đang mở chat với ${user.fullName}.`);
        navigate("/chat");
    };

    const handleTabChange = (tabKey) => startTransition(() => setActiveTab(tabKey));
    const handleRadiusChange = (nextRadius) => startTransition(() => setRadiusKm(nextRadius));

    return (
        <section className="follow-page">
            <Container className="follow-shell">

                <Button
                    as={Link}
                    to="/feed"
                    variant="light"
                    className="d-inline-flex align-items-center gap-2 mb-3"
                >
                    ← Quay lại cộng đồng
                </Button>
                <div className="follow-hero">
                    <div className="follow-hero__panel">
                        <div className="follow-hero__eyebrow">Follow Network</div>
                        <h1>Khám phá runner gần bạn, follow ngay như một social app thật.</h1>
                        <p>Theo dõi những người chạy gần khu vực của bạn, quản lý follower rõ ràng, tìm kiếm theo tên hoặc số điện thoại, và chuyển từ màn “Friends” thô cứng sang một trải nghiệm hiện đại, production-ready.</p>
                    </div>

                    <div className="follow-hero__stats">
                        {visibleStat.map((stat) => {
                            const Icon = stat.icon;
                            return (
                                <div key={stat.label} className="follow-stat">
                                    <div>
                                        <strong>{stat.value}</strong>
                                        <span>{stat.label}</span>
                                    </div>
                                    <Icon size={22} color="#2563eb" />
                                </div>
                            );
                        })}
                    </div>
                </div>

                <div className="follow-list-panel">
                    {notice && <Alert variant="light" className="follow-toast">{notice}</Alert>}

                    <div className="follow-toolbar">
                        <FollowSearchBar value={rawKeyword} onChange={setRawKeyword} />
                        <FollowRadiusFilter value={radiusKm} onChange={handleRadiusChange} options={RADIUS_OPTIONS} />
                    </div>

                    <FollowTabs activeTab={activeTab} onChange={handleTabChange} counts={counts} />

                    <div className="follow-list">
                        {loading && Array.from({ length: 5 }).map((_, index) => <FollowUserSkeleton key={index} />)}
                        {!loading && error && <FollowEmptyState type="error" onRetry={loadFollowNetwork} />}
                        {!loading && !error && items.length === 0 && (
                            <FollowEmptyState type={keyword ? "noResult" : TAB_EMPTY_STATE[activeTab]} onRetry={loadFollowNetwork} />
                        )}
                        {!loading && !error && items.length > 0 && items.map((user) => (
                            <FollowUserCard
                                key={user.id}
                                user={user}
                                isPending={pendingUserIds.includes(user.id)}
                                onToggleFollow={handleFollowToggle}
                                onMessage={handleMessage}
                            />
                        ))}
                    </div>

                    {!loading && !error && totalPages > 1 && (
                        <div className="follow-pagination">
                            <PaginationCustom
                                currentPage={currentPage}
                                totalPages={totalPages}
                                totalElements={totalElements}
                                onPageChange={setCurrentPage}
                            />
                        </div>
                    )}
                </div>
            </Container>
        </section>
    );
};

export default FollowPage;
