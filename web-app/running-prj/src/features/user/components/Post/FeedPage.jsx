import React from "react";
import SidebarLeft from "./SlidebarLeft";
import CreatePost from "./CreatePost";
import PostCard from "./PostCard";
import SidebarRight from "./SlidebarRight";
import "@/style/feed.css";
import { getFeed } from "@/features/admin/users/services/UserService";

export default function FeedPage() {

    // const posts = [
    //     {
    //         id: 1,
    //         user: "Beat Ninh Bình",
    //         time: "25 phút",
    //         content: "Một runner vừa hoàn thành buổi chạy sáng tại Ninh Bình 🏃‍♂️",
    //         distance: "5.2 km",
    //         pace: "5:20/km",
    //         duration: "28 min"
    //     },
    //     {
    //         id: 2,
    //         user: "Beat Hà Nội",
    //         time: "30 phút",
    //         content: "Một runner vừa hoàn thành buổi chạy sáng tại Hà Nội 🏃‍♂️",
    //         distance: "6.5 km",
    //         pace: "5:20/km",
    //         duration: "28 min"
    //     },
    //     {
    //         id: 3,
    //         user: "Beat Đà Nẵng",
    //         time: "45 phút",
    //         content: "Một runner vừa hoàn thành buổi chạy sáng tại Đà Nẵng 🏃‍♂️",
    //         distance: "7.0 km",
    //         pace: "6:20/km",
    //         duration: "28 min"
    //     },
    //     {
    //         id: 4,
    //         user: "Beat Huế",
    //         time: "50 phút",
    //         content: "Một runner vừa hoàn thành buổi chạy sáng tại Huế 🏃‍♂️",
    //         distance: "8.0 km",
    //         pace: "6:10/km",
    //         duration: "28 min"
    //     },
    //     {
    //         id: 5,
    //         user: "Beat Hải Phòng",
    //         time: "50 phút",
    //         content: "Một runner vừa hoàn thành buổi chạy sáng tại Hải Phòng 🏃‍♂️",
    //         distance: "8.0 km",
    //         pace: "6:10/km",
    //         duration: "28 min"
    //     }
    // ];
    const [posts, setPosts] = React.useState([]);

    React.useEffect(() => {
        fetchFeed();
    }, []);

    const fetchFeed = async () => {

        try {
            const res = await getFeed();
            const data = res.data.data;
            setPosts(data);
        } catch (error) {
            console.error("Fail to load feed:", error);
            alert("Cannot load feed from server");
        }
    };

    return (
        <div className="layout">

            <SidebarLeft />

            <div className="feed">

                <CreatePost />

                {posts.map(post => (
                    <PostCard key={post.id} post={post} />
                ))}

            </div>

            <SidebarRight />

        </div>
    );
}