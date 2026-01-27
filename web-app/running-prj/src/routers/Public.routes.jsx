import { LoginPage } from '@/features/auth/pages/LoginPage'
import PublicHomePage from '@/features/public-site/pages/PublicHomePage'
import React from 'react'

const publicRoutes = [
  { path: "/login", element: <LoginPage /> },
  { path: "/register", element: <LoginPage /> },
  { path: "/home", element: <PublicHomePage /> }
]



export default publicRoutes
