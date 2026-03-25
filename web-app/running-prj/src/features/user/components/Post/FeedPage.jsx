import React, { useState, useEffect, useRef, useCallback } from "react";
import SidebarLeft from "./SlidebarLeft";
import CreatePost from "./CreatePost";
import PostCard from "./PostCard";
import SidebarRight from "./SlidebarRight"; 
import "@/style/feed.css";
import { getFeed } from "@/features/admin/users/services/UserService";
import "@/features/user/components/Post/Feed.css";

export default function FeedPage() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(false); 
    
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);

    const observer = useRef();
    const lastPostElementRef = useCallback(
        (node) => {
            if (loading) return;
            if (observer.current) observer.current.disconnect();
            
            observer.current = new IntersectionObserver((entries) => {
                if (entries[0].isIntersecting && hasMore) {
                    setPage((prevPage) => prevPage + 1); 
                }
            });
            
            if (node) observer.current.observe(node);
        },
        [loading, hasMore]
    );

    useEffect(() => {
        fetchFeed(page);
    }, [page]);

    const fetchFeed = async (pageNumber) => {
    try {
        setLoading(true);
        
        const res = await getFeed({ page: pageNumber, size: 5 }); 
        
        const feedData = res.data.data; 
        console.log("Feed data:", feedData); 

        if (!feedData) return;

        const newPosts = feedData.data || [];
        const hasNextPage = feedData.hasNext;

        setHasMore(hasNextPage); 

        setPosts((prevPosts) => {
            return pageNumber === 1 ? newPosts : [...prevPosts, ...newPosts];
        });

    } catch (error) {
        console.error("Fail to load feed:", error);
    } finally {
        setLoading(false);
    }
};

    const handlePostCreated = () => {
        setPage(1); 
        setHasMore(true); 
    };

    return (
        <div className="layout">
            <SidebarLeft />

            <div className="feed">
                <CreatePost onSuccess={handlePostCreated} />

                {posts.length > 0 ? (
                    posts.map((post, index) => {
                        if (posts.length === index + 1) {
                            return (
                                <div ref={lastPostElementRef} key={post.id || index}>
                                    <PostCard post={post} />
                                </div>
                            );
                        } else {
                            return <PostCard key={post.id || index} post={post} />;
                        }
                    })
                ) : (
                    !loading && (
                        <div className="modern-card" style={{ textAlign: "center", color: "var(--text-secondary)", padding: "20px" }}>
                            Chưa có bài viết nào. Hãy là người đầu tiên đăng bài!
                        </div>
                    )
                )}

                {loading && (
                    <div style={{ textAlign: "center", padding: "16px", color: "var(--text-secondary)" }}>
                        <div className="spinner-border text-primary" role="status" style={{ width: "1.5rem", height: "1.5rem", borderWidth: "0.15em" }}></div>
                        <div style={{ marginTop: "8px", fontSize: "14px" }}>Đang tải thêm...</div>
                    </div>
                )}

                {!hasMore && posts.length > 0 && (
                    <div style={{ textAlign: "center", padding: "24px", color: "var(--text-secondary)", fontWeight: 500, fontSize: "15px" }}>
                        Bạn đã xem hết bảng tin ngày hôm nay! 🎉
                    </div>
                )}
            </div>

        </div>
    );
}