import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { Card } from 'react-bootstrap';

export interface HistoricalData {
  dateOfLastTest: string;
  score: number;
}

interface LineChartComponentProps {
  historicalData: HistoricalData[];
  lineColor: string;
  title: string;
}

const LineChartComponent: React.FC<LineChartComponentProps> = ({ historicalData, lineColor, title }) => {
  // Función para formatear la fecha
  const formatDate = (date: string) => {
    const formattedDate = format(new Date(date), 'MMM-yy', { locale: es });
    return formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);
  };

  return (
    <Card style={{ width: '100%', paddingRight:'30px', paddingTop:'20px' }}>
      <h5 style={{ textAlign: 'left', color: lineColor, fontWeight: 'bold', marginLeft: '20px', marginBottom: '10px' }}>
        {title}
      </h5>
      <ResponsiveContainer width="100%" height={200}>
        <LineChart
          data={historicalData}
          margin={{
            top: 20,
            right: 0,
            left: 0,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="dateOfLastTest" tickFormatter={formatDate} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="score" stroke={lineColor} strokeWidth={2} activeDot={{ r: 8 }} />
        </LineChart>
      </ResponsiveContainer>
    </Card>
  );
};

export default LineChartComponent;
