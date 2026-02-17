import PrivateFooter from '@/shared/components/PrivateFooter'
import PrivateHeader from '@/shared/components/PrivateHeader'
import PrivateNavbar from '@/shared/components/PrivateNavbar'
import React, { useEffect } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { authService } from '../services/authService'

const PrivateLayout = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const isAccessToken = authService.getAccessToken() === "ACCESS_TOKEN";
    if (!isAccessToken) {
      navigate("login")
    }
  }, [])

  return (
    <div className="d-flex flex-column min-vh-100">
      <PrivateHeader />

      <main className="flex-fill d-flex">

        {/* Sidebar */}
        <aside className="border-end border-2 px-3" style={{ width: 240 }}>
          <PrivateNavbar />
        </aside>

        {/* Content */}
        <section className="flex-fill p-3">
          <Outlet />
        </section>

      </main>

      <PrivateFooter />
    </div>
  )
}

export default PrivateLayout
