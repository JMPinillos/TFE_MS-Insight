import { NavLink, useNavigate } from "react-router-dom"; // Importa NavLink y useNavigate de react-router-dom
import Nav from "react-bootstrap/Nav"; // Importa Nav de react-bootstrap
import Navbar from "react-bootstrap/Navbar"; // Importa Navbar de react-bootstrap
import { Dropdown } from "react-bootstrap"; // Importa Dropdown de react-bootstrap

import { useAuthContext } from "../../hooks/useAuthContext"; // Importa el hook useAuthContext
import { loginOut } from "../../services/userService"; // Importa la función loginOut
import { userRepository } from "../../data/persistence/InjectionDependencyRepository"; // Importa userRepository
import { useEffect, useState } from "react"; // Importa useEffect y useState de React
import { RoleType } from "../../types";

// Componente NavBar
export const NavBar = () => {
	const { authState, doLogout } = useAuthContext(); // Utiliza el contexto de autenticación

	// Estado para la URL del avatar
	const [avatarUrl, setAvatarUrl] = useState<string>("");

	// useEffect para obtener la URL del avatar del usuario
	useEffect(() => {
		const getAvatar = async () => {
			const avatar = await userRepository.getAvatarUrl(authState.user!.id);
			if (avatar) {
				setAvatarUrl(avatar);
			} else {
				setAvatarUrl(`images/avatars/default.png`); // URL de avatar por defecto
			}
		};
		getAvatar();
	}, [authState.user?.id]);

	const navigate = useNavigate(); // Hook para navegar

	// Función para manejar el cierre de sesión
	const onLogOut = () => {
		loginOut(); // Llama a la función de cierre de sesión
		doLogout(); // Actualiza el estado de autenticación
		navigate("./login", { replace: true }); // Navega a la página de inicio de sesión
	};

	// Renderiza la barra de navegación
	return (
		<>
			<Navbar
				expand="lg"
				className="bg-primary mb-3"
				variant="dark"
				data-bs-theme="dark"
				fixed="top"
			>
				<div className="container-fluid" id="navbar-container">
					<Navbar.Brand>MS-Insight</Navbar.Brand>
					<Navbar.Toggle aria-controls="basic-navbar-nav" />
					<Navbar.Collapse id="basic-navbar-nav">
						<Nav className="me-auto">
							{/* Enlaces de navegación */}
							<NavLink
								to="/dashboard"
								className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}
							>
								Dashboard
							</NavLink>

							{/* Sólo si es admin o researcher puede navegar a la subida de ficheros */}
							{authState.user?.rol === RoleType.ADMIN || authState.user?.rol === RoleType.RESEARCHER ? (
							<NavLink
									to="/uploadfixeddata"
									className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}
								>
									Subir Fixed csv
								</NavLink>
							) : null}



						</Nav>
						<Nav className="ms-auto">
							{/* Dropdown para el avatar y opciones del usuario */}
							<Dropdown align="end">
								<Dropdown.Toggle as="a" bsPrefix="p-0">
									<img
										src={avatarUrl}
										alt="Avatar"
										style={{
											width: '40px',
											height: '40px',
											borderRadius: '50%',
											cursor: 'pointer',
											border: '2px solid rgba(255, 255, 255, 0.2)',
											boxShadow: '0 0 5px rgba(0, 0, 0, 0.2)'
										}}
									/>
								</Dropdown.Toggle>

								<Dropdown.Menu align="end">
									<Dropdown.Item disabled>{authState.user?.name}</Dropdown.Item>
									<Dropdown.Divider />
									<Dropdown.Item onClick={onLogOut}>Salir</Dropdown.Item>
								</Dropdown.Menu>
							</Dropdown>
						</Nav>


					</Navbar.Collapse>
				</div>
			</Navbar>
		</>
	);
};
