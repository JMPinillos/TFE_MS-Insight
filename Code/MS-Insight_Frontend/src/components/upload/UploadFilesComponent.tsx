// src/components/upload/UploadFilesComponent.tsx
import React, { useState } from 'react';
import axios from 'axios';
import { Button, Form, Card, Spinner, Modal } from 'react-bootstrap';
import { useAuthContext } from '../../hooks/useAuthContext';
import { RoleType } from '../../types';
import { uploadFiles } from '../../services/FileService';
import { activateLambda } from '../../services/LambdaService';

const UploadFilesComponent: React.FC = () => {
  const [files, setFiles] = useState<File[]>([]);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingLambda, setLoadingLambda] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [uploadType, setUploadType] = useState('variable'); // Estado para el tipo de subida
  
  const {authState} = useAuthContext(); // Utiliza el contexto de autenticación

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const fileList = event.target.files;
    if (fileList) {
      setFiles(Array.from(fileList));
    }
  };

  const handleUpload = async () => {
    if (files.length === 0) {
      alert('Por favor selecciona algunos archivos para subir.');
      return;
    }

    const formData = new FormData();
    files.forEach(file => {
      formData.append('files', file);
    });

    try {
      setLoading(true); // Activar el loader

      // Subir archivos
      const response = await uploadFiles(uploadType as 'fixed' | 'variable', formData);

      setMessage(`Archivos subidos correctamente: ${response}`);
      setShowModal(true); // Mostrar el modal
    } catch (err) {
      if (axios.isAxiosError(err)) {
        setMessage(`Hubo un error subiendo los archivos: ${err.response?.data}`);
      } else {
        setMessage('Hubo un error subiendo los archivos.');
      }
    } finally {
      setLoading(false); // Desactivar el loader
    }
  };

  const handleCloseModal = () => setShowModal(false);
  const handleActivateLanda = async () => {
    setLoadingLambda(true);
    console.log('Activate Landa');

    try {
      const response = await activateLambda();
      console.log(response);
      setMessage('Proceso de actualización finalizado correctamente.');
      
    } catch (err) {

      if (axios.isAxiosError(err)) {
        setMessage(`Hubo un error al lanzar el proceso de actualización de base de datos:  ${err.response?.data}`);
      } else {
        setMessage('Hubo un error al lanzar el proceso de actualización de base de datos');
      }
      
    } finally {
      setLoadingLambda(false);
      setShowModal(false);
    }

    
    
  };

  const handleUploadTypeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUploadType(event.target.value);
  };

  return (
    <Card style={{ width: '100%', padding: '20px' }}>
      {authState.user?.rol === RoleType.ADMIN && (
        <Form.Group className='mt-3'>
          <div style={{ display: 'flex' }}>
            <Form.Check
              type="radio"
              id="fixed"
              name="uploadType"
              value="fixed"
              checked={uploadType === 'fixed'}
              onChange={handleUploadTypeChange}
            />
            <Form.Label htmlFor="fixed" style={{ marginLeft: '8px' }}>Subir archivos fijos</Form.Label>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', marginTop: '10px' }}>
            <Form.Check
              type="radio"
              id="variable"
              name="uploadType"
              value="variable"
              checked={uploadType === 'variable'}
              onChange={handleUploadTypeChange}
            />
            <Form.Label htmlFor="variable" style={{ marginLeft: '8px' }}>Subir archivos variables</Form.Label>
          </div>
        </Form.Group>
      )}

      <Form className='mt-3'>
        <Form.Group controlId="formFileMultiple" className="mb-3">
          <Form.Label>Selecciona archivos CSV</Form.Label>
          <Form.Control type="file" multiple onChange={handleFileChange} disabled={loading} />
        </Form.Group>
        <Button variant="primary" onClick={handleUpload} disabled={loading}>
          {loading ? (
            <>
              <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
              {' '}Subiendo archivos...
            </>
          ) : 'Subir Archivos'}
        </Button>
      </Form>
      {message && <div className={`mt-3 alert ${message.includes('error') ? 'alert-danger' : 'alert-success'}`}>{message}</div>}
      
      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>Subida Exitosa</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Los archivos se han subido correctamente. Ahora puedes actualizar la base de datos.
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleActivateLanda} disabled={loadingLambda}>
            {loadingLambda ? (
              <>
                <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                {' '}Actualizando base de datos...
              </>
            ) : 'Lanzar proceso de actualización de base de datos'}
          </Button>
          <Button variant="secondary" onClick={handleCloseModal}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>
    </Card>
  );
};

export default UploadFilesComponent;
