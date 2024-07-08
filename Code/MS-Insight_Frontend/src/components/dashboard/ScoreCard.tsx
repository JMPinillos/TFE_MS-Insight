import React from 'react';
import { Card, ProgressBar } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretDown, faCaretUp } from '@fortawesome/free-solid-svg-icons';

// Define la estructura de los datos que esperas recibir en los props
export interface ScoreDetails {
  percentageScore: number;
  minimumScore: number;
  maximumScore: number;
  evolution: number;
  dateOfLastTest: string;
  title: string;
  score: number;
  color: string;
}

export interface ScoresCardProps {
  details: ScoreDetails;
}


const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('es-ES', { day: 'numeric', month: 'long', year: 'numeric' }).format(date);
};

export const ScoresCard: React.FC<ScoresCardProps> = ({ details }) => {

  const formattedDate = formatDate(details.dateOfLastTest);

  return (
    <Card style={{ width: '18rem' }}>
      <Card.Body>
        <Card.Title style={{ color: details.color, fontWeight: 'bold' }}>{details.title}</Card.Title>
        <span style={{ color: details.color, fontWeight: 'bold', fontSize: '1.5rem' }}>
          {details.score != null ? details.score.toFixed(2) : ""}
        </span>
        {/* TODO: color de la barra como el del test */}
        <ProgressBar now={details.percentageScore} style={{ backgroundColor: '#eeeeee', color: '#000000' }} striped variant="custom" />
        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '5px' }}>
          <div>
            {details.evolution === 0 ? '' : (
              details.evolution > 0 ?
                <FontAwesomeIcon icon={faCaretUp} style={{ color: 'green', marginTop: '5px' }} /> :
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginTop: '5px' }} />
            )}
            <strong style={{ marginLeft: '5px' }}>{details.percentageScore != null ? details.percentageScore.toFixed(2) : ""}%</strong>
          </div>
          <small>{formattedDate}</small>
        </div>
      </Card.Body>
    </Card>
  );
};
