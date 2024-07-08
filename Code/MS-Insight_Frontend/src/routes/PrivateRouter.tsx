import { Container } from "react-bootstrap";
import { Routes, Route, Navigate } from "react-router-dom";
import { NavBar } from "../components/navBar/Navbar";
import { DashboardPage } from "../pages/DashboardPage";
import { Error404Page } from "../pages/Error404Page";
// import UploadPage from "../pages/UploadPage";
import UploadFixedDataPage from "../pages/UploadFixedDataPage";



export const PrivateRouter = () => {
	return (
		<>
			<NavBar />
			<Container className="main-container">
				<Routes>
					<Route path="/" element={<Navigate to="/dashboard" />} />
					<Route path="/dashboard" element={<DashboardPage />} />
					{/* <Route path="/upload" element={<UploadPage />} /> */}
					<Route path="/uploadfixeddata" element={<UploadFixedDataPage />} />
					
					{/* Si navega a otra ruta, error */}
					<Route path="/notfound" element={<Error404Page />} />
					<Route path="/*" element={<Navigate to="/notfound" />} />
				</Routes>
			</Container>
		</>
	);
};
