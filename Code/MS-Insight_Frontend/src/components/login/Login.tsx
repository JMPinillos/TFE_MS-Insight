import { Form, Button, Alert } from 'react-bootstrap'; // Importa componentes de react-bootstrap
import { useLoginController } from '../../hooks/useLoginController'; // Importa el hook useLoginController

// Componente Login
const Login: React.FC = () => {
    // Extrae las propiedades y funciones del hook useLoginController
    const { handleLoginUser, 
        password,
        setPassword,
        email,
        setEmail,
        loginError
    } = useLoginController();

    return (
        // Formulario de inicio de sesión
        <Form onSubmit={(event) => {handleLoginUser(event)}}>
            {/* Grupo del formulario para el usuario (correo electrónico) */}
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Usuario</Form.Label>
                <Form.Control 
                    type="text" 
                    placeholder="Ingresa tu usuario" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                />
            </Form.Group>

            {/* Grupo del formulario para la contraseña */}
            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Contraseña</Form.Label>
                <Form.Control 
                    type="password" 
                    placeholder="Contraseña" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)} 
                />
            </Form.Group>

            {/* Botón para enviar el formulario */}
            <div className="d-flex justify-content-center">
                <Button type="submit">
                    Iniciar Sesión
                </Button>
            </div>

            {/* Alerta de error de inicio de sesión */}
            {loginError && (
                <Alert variant="danger" className="mt-3">
                    {loginError}
                </Alert>
            )}
        </Form>
    );
};

export default Login;
