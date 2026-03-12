import React, { useState } from "react";
import { Navbar, Container, Offcanvas, Button } from "react-bootstrap";
import { List } from "react-bootstrap-icons";

const drawerWidth = 300;

function Scene({ children }) {

  const [showSidebar, setShowSidebar] = useState(false);

  const handleClose = () => setShowSidebar(false);
  const handleShow = () => setShowSidebar(true);

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        height: "100vh"
      }}
    >

      {/* HEADER */}

      <Navbar bg="dark" variant="dark" fixed="top">

        <Container fluid>

          <Button
            variant="outline-light"
            className="d-sm-none me-2"
            onClick={handleShow}
          >
            <List size={22} />
          </Button>

          <Navbar.Brand>
            Chat Application
          </Navbar.Brand>

        </Container>

      </Navbar>


      {/* BODY */}

      <div
        style={{
          display: "flex",
          marginTop: "56px",
          height: "calc(100vh - 56px)"
        }}
      >

        {/* SIDEBAR DESKTOP */}

        <div
          className="d-none d-sm-block border-end"
          style={{
            width: drawerWidth
          }}
        >
        </div>


        {/* SIDEBAR MOBILE */}

        <Offcanvas
          show={showSidebar}
          onHide={handleClose}
          responsive="sm"
        >
          <Offcanvas.Header closeButton>
            <Offcanvas.Title>
              Menu
            </Offcanvas.Title>
          </Offcanvas.Header>

          <Offcanvas.Body>

          </Offcanvas.Body>

        </Offcanvas>


        {/* MAIN CONTENT */}

        <main
          style={{
            flexGrow: 1,
            overflow: "auto",
            display: "flex",
            justifyContent: "center",
            width: "100%"
          }}
        >
          {children}
        </main>

      </div>

    </div>
  );
}

export default Scene;