import Footer from '@/shared/Footer'
import RunWiseNavbar from '@/shared/RunWiseNavbar'
import React from 'react'
import { Outlet } from 'react-router-dom'

const PublicLayouts = () => {
  return (
    <div className='d-flex flex-column min-vh-100'>
      <RunWiseNavbar />
      <main className='flex-fill'>
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export default PublicLayouts
