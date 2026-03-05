import React from 'react'
import Hero from './Hero'
import { Routes } from 'react-router-dom'
import Community from './Post/Community'
import Gear from './Gear'
import Blog from './Blog'

const HomePage = () => {
    return (
        <div>
            <Hero />
            <Routes />
            <Community />
            <Gear />
            <Blog />
        </div>
    )
}

export default HomePage
