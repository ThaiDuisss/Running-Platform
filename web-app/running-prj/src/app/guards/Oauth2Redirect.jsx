import React, { useContext, useEffect, useRef } from 'react'
import { AuthActionContext } from '../providers/AuthProvider'
import { useNavigate } from 'react-router-dom';

const Oauth2Redirect = () => {
  const { checkAuth } = useContext(AuthActionContext);
  const navigate = useNavigate();

  useEffect(() => {

    const token = localStorage.getItem("AccessToken");
    if (token) {
      checkAuth();
      navigate("/");
    }

  }, []);

  return (
    <div>
      <p>Logging in...</p>;
    </div>
  )
}

export default Oauth2Redirect
