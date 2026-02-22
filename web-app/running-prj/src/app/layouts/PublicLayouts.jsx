import Footer from '@/shared/Footer'
import RunWiseNavbar from '@/shared/RunWiseNavbar'
import React from 'react'
import { Outlet } from 'react-router-dom'

const PublicLayouts = () => {
  return (
    <>
      <RunWiseNavbar />
      <main>
        <Outlet />
      </main>
      <Footer />
    </>
  )
}

export default PublicLayouts
