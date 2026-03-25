import Community from "@/features/user/components/Post/Community"
import PublicLayouts from "../layouts/PublicLayouts"
import FeedPage from "@/features/user/components/Post/FeedPage"
import PlanPage from "@/features/user/components/Plans/PlanPage"
import ProfilePage from "@/features/user/components/Profile/ProfilePage"
import FriendPage from "@/features/user/components/Friends/FriendPage"
import Chat from "@/features/user/components/Chat/components/Chat"
import ProtectedRoute from "./ProtectedRoute"
import AnimatedLayout from "../layouts/AnimatedLayout"
import CustomPlan from "@/features/user/components/Plans/CustomPlan"
import SchedulePage from "@/features/user/components/Plans/SchedulePage"
import ReviewPlan from "@/features/user/components/Plans/ReviewPlan"

const userRoutes = [
    {
        path: "/", element:
            <ProtectedRoute roles={["USER", "ADMIN"]}>
                <PublicLayouts />
            </ProtectedRoute>
        ,
        children: [

            {
                path: "/community",
                element: <Community />,
            },

            { path: "/feed", element: <FeedPage /> },

            {
                path: "/plans",
                element: <PlanPage />
            },
            {
                path: "/profile",
                element: <ProfilePage />
            },
            { path: "/follow", element: <FriendPage /> },
            { path: "/friends", element: <FriendPage /> },
            { path: "/chat", element: <Chat /> },
            {
                path: "/plans/custom-plan", element: <AnimatedLayout />, children: [
                    {
                        index: true,
                        element: <CustomPlan />
                    },
                    {
                        path: "/plans/custom-plan/schedule",
                        element: <SchedulePage />
                    },
                    {
                        path: "/plans/custom-plan/review",
                        element: <ReviewPlan />
                    },
                ]
            },

        ]
    }
]

export default userRoutes
