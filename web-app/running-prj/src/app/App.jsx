import React from 'react';
import '@/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { RouterProvider } from 'react-router-dom';
import routes from './routes/Routes';


function App() {
  return (
    <RouterProvider router={routes} ></RouterProvider>
  );
}

export default App;