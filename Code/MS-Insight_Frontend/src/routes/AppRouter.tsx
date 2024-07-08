import { Routes, Route } from "react-router-dom";
import { CheckPrivateRoutes } from "./CheckPrivateRoutes";
import { CheckPublicRoutes } from "./CheckPublicRoutes";
import { PrivateRouter } from "./PrivateRouter";

import { LoginPage } from "../pages/LoginPage";


export const AppRouter = () => {
	return (
		<Routes>
			<Route
				path="login/*"
				element={
					<CheckPublicRoutes>
						<Routes>
							<Route path="/*" element={<LoginPage />} />
						</Routes>
					</CheckPublicRoutes>
				}
			/>
			<Route
				path="/*"
				element={
					<CheckPrivateRoutes>
						<PrivateRouter />
					</CheckPrivateRoutes>
				}
			/>
		</Routes>
	);
};
