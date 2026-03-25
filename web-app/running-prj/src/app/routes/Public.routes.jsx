import HomePage from '@/features/user/components/HomePage'
import React from 'react'
import PublicLayouts from '../layouts/PublicLayouts'
import UserLogin from '../guards/UserLogin'
import Routes from '@/features/user/components/Routes'
import Community from '@/features/user/components/Post/Community'
import Blog from '@/features/user/components/Blog'
import BlogDetail from '@/features/user/components/BlogDetail'
import FeedPage from '@/features/user/components/Post/FeedPage'
import PlanPage from '@/features/user/components/Plans/PlanPage'
import Register from '../guards/Register'
import ProfilePage from '@/features/user/components/Profile/ProfilePage'
import ForgotPassword from '../guards/ForgotPassword'
import ResetPassword from '../guards/ResetPassword'
import Oauth2Redirect from '../guards/Oauth2Redirect'
import { oauthRedirectLoader } from './LoadFile/oauthRedirectLoader'
import Chat from '@/features/user/components/Chat/components/Chat'
import FriendPage from '@/features/user/components/Friends/FriendPage'
import Unauthorized from '@/features/user/components/Unauthorized'

const publicRoutes = [
  { path: "/login", element: <UserLogin /> },
  { path: "/register", element: <Register /> },
  { path: "/forgot-password", element: <ForgotPassword /> },
  { path: "/reset-password", element: <ResetPassword /> },
  { path: "/unauthorized", element: <Unauthorized /> },
  { path: "/oauth2/redirect", element: <Oauth2Redirect />, loader: oauthRedirectLoader },
  {
    path: "/", element: <PublicLayouts />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "/routes", element: <Routes /> },
      { path: "/feed", element: <FeedPage /> },
      { path: "/plans", element: <PlanPage /> },
      { path: "/profile", element: <ProfilePage /> },
      { path: "/follow", element: <FriendPage /> },
      { path: "/friends", element: <FriendPage /> },
      {
        path: "/blog",
        element: <Blog />
      },
      {
        path: "/blog/:id",
        element: <BlogDetail />
      },
    ]
  }
]


export default publicRoutes
