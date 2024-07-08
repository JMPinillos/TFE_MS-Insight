import { Container, Row, Col, Image } from 'react-bootstrap'; // Importaciones de React Bootstrap para el diseño
import Login from '../components/login/Login'; // Importación del componente Login

// Componente LoginPage
export const LoginPage = () => {
    return (
        <Container className="mt-5">
            {/* Fila para el logo y el texto de bienvenida */}
            <Row className="justify-content-md-center">
                <Col md={6} className="text-center mb-4">
                    {/* Imagen del logo */}
                    {/* Asegúrate de tener un archivo de logo en tu proyecto */}
                    <Image src="images/logo.png" alt="Agile Task Flow Logo" width={100} className='rounded' />

                    {/* Título y descripción de la aplicación */}
                    <h1>MS-Insight</h1>
                    <p className="lead">
                        Gestiona tus tareas de manera eficiente y ágil.
                    </p>
                    <p>
                        <i>Organiza, planifica y ejecuta tus proyectos sin complicaciones.</i>
                    </p>
                </Col>
            </Row>

            {/* Fila para el componente de inicio de sesión */}
            <Row className="justify-content-md-center">
                <Col md={4}>
                    {/* Componente Login para iniciar sesión */}
                    <Login />
                </Col>
            </Row>
        </Container>
    );
};
