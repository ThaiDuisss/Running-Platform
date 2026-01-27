import React from 'react';
import './index.css';
import RunWiseNavbar from './components/RunWiseNavbar';
import Hero from './components/Hero';
import Routes from './components/Routes';
import Community from './components/Community';
import Gear from './components/Gear';
import Blog from './components/Blog';
import Footer from './components/Footer';
import 'bootstrap/dist/css/bootstrap.min.css';


function App() {
  return (
    <div className="runWise-wrapper">
      <RunWiseNavbar />
      <Hero />
      <Routes />
      <Community />
      <Gear />
      <Blog />
      <Footer />
    </div>
  );
}

export default App;