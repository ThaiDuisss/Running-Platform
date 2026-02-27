import HomePage from '@/features/user/components/HomePage'
import React from 'react'
import PublicLayouts from '../layouts/PublicLayouts'
import UserLogin from '../guards/UserLogin'
import Routes from '@/features/user/components/Routes'
import Community from '@/features/user/components/Post/Community'
import Blog from '@/features/user/components/Blog'
import FeedPage from '@/features/user/components/Post/FeedPage'
import PlanPage from '@/features/user/components/Plans/PlanPage'

const publicRoutes = [
  { path: "/login", element: <UserLogin /> },
  {
    path: "/", element: <PublicLayouts />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "/routes", element: <Routes /> },
      {
        path: "/community", element: <Community />,
      },
      { path: "feed", element: <FeedPage /> },
      {
        path: "/blog", element: <Blog />
      },
      {
        path: "/plans", element: <PlanPage />
      },

    ]
  }
]



export default publicRoutes
