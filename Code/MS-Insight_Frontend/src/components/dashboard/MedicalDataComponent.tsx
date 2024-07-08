import React from 'react';
import { Card, ListGroup } from 'react-bootstrap';
import { medicalConsultationsDto } from '../../types';

interface MedicalDataComponentProps {
  medicalData: medicalConsultationsDto[];
}

const MedicalDataComponent: React.FC<MedicalDataComponentProps> = ({ medicalData }) => {

  return (
    <Card style={{ width: '100%'}}>
      <Card.Header>Datos Médicos</Card.Header>
      <ListGroup variant="flush">
        {medicalData.map((data, index) => (
          // Renderiza solo el valor con un salto de línea para cada valor.
          // Si deseas incluir un salto de línea visual en el HTML, puedes usar <br/> o separarlos en elementos distintos.
          <ListGroup.Item key={index}><strong>{data.title}</strong>: {data.value}</ListGroup.Item>
        ))}
      </ListGroup>
    </Card>
  );
};

export default MedicalDataComponent;
