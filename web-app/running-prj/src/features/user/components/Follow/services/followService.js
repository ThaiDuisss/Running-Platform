import axiosClient from "@/shared/services/axiosClient";

const BASE = "/api/friends";

export const followService = {
    async getFollowNetwork({ tab, keyword, radiusKm, page = 0, size = 20 }) {
        const response = await axiosClient.get(`${BASE}/network`, {
            params: {
                tab,
                keyword,
                radiusKm,
                page,
                size,
            },
        });

        const data = response?.data?.data;
        const pageData = data?.page;

        return {
            items: (pageData?.content || []).map((user) => ({
                ...user,
                avatarUrl: user.imageUrl,
                isFollowing: user.following,
                isFollower: user.follower,
            })),
            counts: {
                discover: data?.discoverCount || 0,
                following: data?.followingCount || 0,
                followers: data?.followersCount || 0,
            },
            page: pageData,
        };
    },

    async toggleFollow(userId, isFollowing) {
        if (isFollowing) {
            await axiosClient.delete(`${BASE}/follow/${userId}`);
            return;
        }

        await axiosClient.post(`${BASE}/follow/${userId}`);
    },
};
