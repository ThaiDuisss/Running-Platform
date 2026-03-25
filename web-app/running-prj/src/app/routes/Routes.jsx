import { createBrowserRouter } from "react-router-dom";
import publicRoutes from "./Public.routes";
import adminRoutes from "./Admin.routes";
import userRoutes from "./User.routes";


const routes = createBrowserRouter([...publicRoutes, ...adminRoutes, ...userRoutes])

export default routes;