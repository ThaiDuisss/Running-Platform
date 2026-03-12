import React from 'react';
import '@/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { RouterProvider } from 'react-router-dom';
import routes from './routes/Routes';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "bootstrap-icons/font/bootstrap-icons.css";
function App() {
  return (
    <>
      <RouterProvider router={routes} ></RouterProvider>
      <ToastContainer
        position="top-right"
        autoClose={3000}
      />
    </>

  );
}

export default App;