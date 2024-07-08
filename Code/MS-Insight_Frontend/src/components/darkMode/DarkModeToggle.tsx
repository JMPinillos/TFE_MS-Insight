import { Dropdown } from 'react-bootstrap'; // Importa Dropdown de react-bootstrap
import 'bootstrap-icons/font/bootstrap-icons.css'; // Importa los estilos de los iconos de bootstrap

// Componente DarkModeToggle como una función de componente de React
const DarkModeToggle: React.FC = () => {
    // Función para cambiar el tema y almacenarlo en localStorage
    const setTheme = (theme: string) => {
      document.body.setAttribute('data-bs-theme', theme); // Establece el atributo en el body para aplicar el tema
      localStorage.setItem("data-bs-theme", theme); // Guarda el tema seleccionado en localStorage
    };

    // Aplica el tema guardado en localStorage cuando el componente se monta o se actualiza
    const dataBsTheme = localStorage.getItem("data-bs-theme");
    if (dataBsTheme) {
      setTheme(dataBsTheme);
    }

    // Renderiza un Dropdown para cambiar entre modos claro y oscuro
    return (
      <Dropdown className='position-fixed bottom-0 end-0 mb-3 me-3 bd-mode-toggle' style={{ zIndex: 999 }}>
        {/* Botón del Dropdown */}
        <Dropdown.Toggle variant="secondary">
          <i className="bi bi-circle-half"></i> {/* Ícono del interruptor */}
        </Dropdown.Toggle>

        {/* Menú desplegable con opciones de tema */}
        <Dropdown.Menu>
          <Dropdown.Item onClick={() => setTheme('light')}>
            <i className="bi bi-sun"></i> <span>Light</span> {/* Opción para el modo claro */}
          </Dropdown.Item>
          <Dropdown.Item onClick={() => setTheme('dark')}>
           <i className="bi bi-moon-stars-fill"></i> <span>Dark</span> {/* Opción para el modo oscuro */}
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>
    );
};

// Exporta el componente DarkModeToggle
export default DarkModeToggle;
