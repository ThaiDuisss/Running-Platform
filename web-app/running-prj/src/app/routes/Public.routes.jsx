import HomePage from '@/features/user/components/HomePage'
import React from 'react'
import PublicLayouts from '../layouts/PublicLayouts'
import UserLogin from '../guards/UserLogin'
import Routes from '@/features/user/components/Routes'
import Community from '@/features/user/components/Post/Community'
import Blog from '@/features/user/components/Blog'
import FeedPage from '@/features/user/components/Post/FeedPage'
import PlanPage from '@/features/user/components/Plans/PlanPage'
import { redirect } from 'react-router-dom'
import Register from '../guards/Register'
import ProfilePage from '@/features/user/components/Profile/ProfilePage'
import ForgotPassword from '../guards/ForgotPassword'
import ResetPassword from '../guards/ResetPassword'
import Oauth2Redirect from '../guards/Oauth2Redirect'
import { oauthRedirectLoader } from './LoadFile/oauthRedirectLoader'
import Chat from '@/features/user/components/Chat/components/Chat'

// ✅ loader đúng
const communityLoader = () => {
  if (localStorage.getItem("ACCESS-TOKEN")) {
    throw redirect("/feed");
  }
  return null;
}

const publicRoutes = [
  { path: "/login", element: <UserLogin /> },
  { path: "/register", element: <Register /> },
  { path: "/forgot-password", element: <ForgotPassword /> },
  { path: "/reset-password", element: <ResetPassword /> },
  { path: "/oauth2/redirect", element: <Oauth2Redirect />, loader: oauthRedirectLoader },
  {
    path: "/", element: <PublicLayouts />,
    children: [
      { index: true, element: <HomePage /> }, 
      { path: "/routes", element: <Routes /> },

      {
        path: "/community",
        element: <Community />,
        loader: communityLoader
      },

      { path: "/feed", element: <FeedPage /> },

      {
        path: "/blog",
        element: <Blog />
      },

      {
        path: "/plans",
        element: <PlanPage />
      },
      {
        path: "/profile",
        element: <ProfilePage />
      },
      { path: "/chat", element: <Chat /> },

    ]
  }
]


export default publicRoutes