// Importaciones de React y otros componentes/librerías
import React, { useEffect, useState } from 'react';
import { Card, Col, Dropdown, Row } from 'react-bootstrap';
import { getPatients, getScores } from '../services/DashboardService'; // Importando función getPatients del servicio DashboardService
import LineChartComponent, { HistoricalData } from '../components/dashboard/LineChartComponent'; // Importando componente LineChartComponent y tipo HistoricalData del archivo LineChartComponent.tsx
import { ScoresGroupDto, TestHistoryDto, TestHistoryMsQoL54Dto } from '../types'; // Importando tipo TestHistoryDto del archivo types.ts
import { ScoresCard } from '../components/dashboard/ScoreCard';
import LineChartMSQoL54Component, { HistoricalDataMsQoL54 } from '../components/dashboard/LineChartMSQoL54Component';
import MedicalDataComponent from '../components/dashboard/MedicalDataComponent';


// Componente Dashboard
export const DashboardPage = () => {
  const [patients, setPatients] = useState<number[]>([]);
  const [filteredPatients, setFilteredPatients] = useState<number[]>([]);
  const [selectedPatient, setSelectedPatient] = useState<number | null>(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [scores, setScores] = useState<ScoresGroupDto>();


  useEffect(() => {
    const fetchPatients = async () => {
      const patientsData = await getPatients();
      setPatients(patientsData);
      setFilteredPatients(patientsData);
      if (patientsData.length > 0) {
        setSelectedPatient(patientsData[0]);
      }
    };
    fetchPatients();
  }, []);

  useEffect(() => {
    if (searchTerm === '') {
      setFilteredPatients(patients);
    } else {
      const results = patients.filter(patient =>
        patient.toString() == searchTerm
      );
      setFilteredPatients(results);
    }
  }, [searchTerm, patients]);


  useEffect(() => {
    const fetchScores = async () => {
      try {

        if (selectedPatient) {
          // Obtener scores
          const scoresData = await getScores(selectedPatient);
          setScores(scoresData);
        }

      } catch (error) {
        console.error(`Error al cargar los scores del paciente ${selectedPatient}:`, error);
      }
    };

    if (selectedPatient) {
      fetchScores();
    }
  }, [selectedPatient]);

  const handleSelect = (eventKey: string | null) => {
    const patientNumber = eventKey ? parseInt(eventKey, 10) : null;
    setSelectedPatient(patientNumber);
    setShowDropdown(false); // Cierra el menú desplegable tras la selección
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      if (filteredPatients.length > 0) {
        handleSelect(filteredPatients[0].toString());
      }
    }
  };

  // Función para convertir los datos
  const transformData = (data: TestHistoryDto[]): HistoricalData[] => {
    return data.map(test => ({
      dateOfLastTest: test.completed,
      score: test.score,
    }));
  };

  // Función para convertir los datos
  const transformDataMSQoL54 = (data: TestHistoryMsQoL54Dto[]): HistoricalDataMsQoL54[] => {
    return data.map(test => ({
      dateOfLastTest: test.completed,
      physicalHealth: test.physicalHealth,
      mentalHealth: test.mentalHealth,
    }));
  };

  // Renderiza el componente Dashboard con la información de las tareas
  // **** QUIERO QUE EL TITULO LO COJA DE LA BASE DE DATOS **** //
  return (
    <>
      <h4>Dashboard</h4>
      {selectedPatient && scores &&
        <>
          <Row className='mt-4'>
            <Col md="12" className="d-flex flex-row justify-content-between">
              <ScoresCard details={{ score: scores!.msiS29.score, percentageScore: scores!.msiS29.percentageScore, minimumScore: scores!.msiS29.minimumScore, maximumScore: scores!.msiS29.maximumScore, evolution: scores!.msiS29.evolution, dateOfLastTest: scores!.msiS29.dateOfLastTest!, title: scores!.msiS29.title, color: "#fe5722" }} />
              <ScoresCard details={{ score: scores!.haq.score, percentageScore: scores!.haq.percentageScore, minimumScore: scores!.haq.minimumScore, maximumScore: scores!.haq.maximumScore, evolution: scores!.haq.evolution, dateOfLastTest: scores!.haq.dateOfLastTest!, title: scores!.haq.title, color: "#01a8dd" }} />
              <ScoresCard details={{ score: scores!.neuroQoLSF.score, percentageScore: scores!.neuroQoLSF.percentageScore, minimumScore: scores!.neuroQoLSF.minimumScore, maximumScore: scores!.neuroQoLSF.maximumScore, evolution: scores!.neuroQoLSF.evolution, dateOfLastTest: scores!.neuroQoLSF.dateOfLastTest!, title: scores!.neuroQoLSF.title, color: "#389500" }} />
              <ScoresCard details={{ score: scores!.neuroQoLCOG.score, percentageScore: scores!.neuroQoLCOG.percentageScore, minimumScore: scores!.neuroQoLCOG.minimumScore, maximumScore: scores!.neuroQoLCOG.maximumScore, evolution: scores!.neuroQoLCOG.evolution, dateOfLastTest: scores!.neuroQoLCOG.dateOfLastTest!, title: scores!.neuroQoLCOG.title, color: "#bc01cd" }} />
              <ScoresCard details={{ score: scores!.fss.score, percentageScore: scores!.fss.percentageScore, minimumScore: scores!.fss.minimumScore, maximumScore: scores!.fss.maximumScore, evolution: scores!.fss.evolution, dateOfLastTest: scores!.fss.dateOfLastTest!, title: scores!.fss.title, color: "#5622fd" }} />
            </Col>
          </Row>

          <Row style={{ marginTop:'10px' }}>
            <Col md={2}>
              <Card>
                <Card.Body>
                <Dropdown className="mb-3 mt-4" show={showDropdown} onToggle={(isOpen) => setShowDropdown(isOpen)} onSelect={handleSelect}>
                <Dropdown.Toggle variant="primary" id="dropdown-basic" style={{ width: '100%', backgroundColor: '#007bff' }}>
                  {selectedPatient ? `Paciente: ${selectedPatient}` : 'Selecciona un paciente'}
                </Dropdown.Toggle>

                <Dropdown.Menu className="dropdown-menu-custom">
                  <input
                    type="search"
                    placeholder="Buscar paciente..."
                    className="search-box"
                    onChange={(e) => setSearchTerm(e.target.value)}
                    onKeyDown={handleKeyDown} // Agrega el manejador para el evento onKeyDown
                    onClick={(e) => e.stopPropagation()} // Evita que el menú se cierre al hacer clic

                    autoFocus
                  />
                  {filteredPatients.map(patient => (
                    <Dropdown.Item key={patient} eventKey={patient.toString()}>
                      {patient}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
              {!scores?.medicalConsultations || scores.medicalConsultations.length == 0 && (
                <div className='alert alert-info'>No hay información de consulta</div>
              )}
                </Card.Body>
              
              </Card>
              

              {scores?.medicalConsultations && scores.medicalConsultations.length > 0 && (
                <MedicalDataComponent medicalData={scores.medicalConsultations} />
              )}
            </Col>
            <Col md={10}>
              <Row style={{ marginTop: '25px'}}>
                <Col md={6}>
                  {scores && scores.msiS29.historical.length > 0 && (
                    <LineChartComponent historicalData={transformData(scores.msiS29.historical)} lineColor={'#fe5722'} title={scores!.msiS29.title} />
                  )}
                </Col>
                <Col md={6}>
                  {scores && scores.haq.historical.length > 0 && (
                    <LineChartComponent historicalData={transformData(scores.haq.historical)} lineColor={'#01a8dd'} title={scores!.haq.title} />
                  )}
                </Col>
              </Row>
              <Row style={{ marginTop: '25px'}}>
                <Col md={6}>
                  {scores && scores.neuroQoLSF.historical.length > 0 && (
                    <LineChartComponent historicalData={transformData(scores.neuroQoLSF.historical)} lineColor={'#389500'} title={scores!.neuroQoLSF.title} />
                  )}
                </Col>
                <Col md={6}>
                  {scores && scores.neuroQoLCOG.historical.length > 0 && (
                    <LineChartComponent historicalData={transformData(scores.neuroQoLCOG.historical)} lineColor={'#bc01cd'} title={scores!.neuroQoLCOG.title} />
                  )}
                </Col>
              </Row>
              <Row style={{ marginTop: '25px'}}>
                <Col md={6}>
                  {scores && scores.fss.historical.length > 0 && (
                    <LineChartComponent historicalData={transformData(scores.fss.historical)} lineColor={'#5622fd'} title={scores!.fss.title} />
                  )}
                </Col>
                <Col md={6}>
                  {scores && scores.msQoL54 && scores.msQoL54.historical.length > 0 && (
                    <LineChartMSQoL54Component historicalData={transformDataMSQoL54(scores.msQoL54.historical)} lineColor={'#'} title={scores!.msQoL54.title} />
                  )}
                </Col>
              </Row>
            </Col>
          </Row>
        </>

      }
    </>
  );
};
