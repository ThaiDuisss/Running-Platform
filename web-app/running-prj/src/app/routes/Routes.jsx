import { createBrowserRouter } from "react-router-dom";
import publicRoutes from "./Public.routes";


const routes = createBrowserRouter([...publicRoutes])

export default routes;