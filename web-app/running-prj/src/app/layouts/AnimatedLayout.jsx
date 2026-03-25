import { AnimatePresence, motion } from 'framer-motion'
import React from 'react'
import { Outlet, useLocation } from 'react-router-dom'

const AnimatedLayout = () => {
    const location = useLocation();
    return (
        <AnimatePresence model="wait">
            <motion.div key={location.pathname} initial={{ opacity: 0, x: 50 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -50 }} transition={{ duration: 0.3 }}>
                <Outlet />
            </motion.div>
        </AnimatePresence>
    )
}

export default AnimatedLayout
