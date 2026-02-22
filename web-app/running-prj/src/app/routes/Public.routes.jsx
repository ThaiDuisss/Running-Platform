import HomePage from '@/features/user/components/HomePage'
import React from 'react'
import PublicLayouts from '../layouts/PublicLayouts'
import UserLogin from '../guards/UserLogin'
import Routes from '@/features/user/components/Routes'
import Community from '@/features/user/components/Community'
import Blog from '@/features/user/components/Blog'

const publicRoutes = [
  { path: "/login", element: <UserLogin /> },
  // { path: "/register", element: <LoginPage /> },
  {
    path: "/", element: <PublicLayouts />,
    children: [
      { index: true, path: "/", element: <HomePage /> },
      { index: true, path: "/routes", element: <Routes /> },
      { index: true, path: "/community", element: <Community /> },
      { index: true, path: "/blog", element: <Blog /> },
    ]
  }
]



export default publicRoutes
