import { createBrowserRouter } from "react-router-dom";
import publicRoutes from "./Public.routes";
import adminRoutes from "./Admin.routes";


const routes = createBrowserRouter([...publicRoutes, ...adminRoutes])

export default routes;