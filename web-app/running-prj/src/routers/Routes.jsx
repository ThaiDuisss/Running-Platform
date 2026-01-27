import { useRoutes } from "react-router-dom";
import publicRoutes from "./Public.routes";


const routes = [
    ...publicRoutes,
]

export const AppRoutes = useRoutes(routes);