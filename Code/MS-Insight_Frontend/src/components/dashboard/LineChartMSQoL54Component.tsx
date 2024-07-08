import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

export interface HistoricalDataMsQoL54 {
  dateOfLastTest: string;
  physicalHealth: number;  
  mentalHealth: number;
}

interface LineChartComponentProps {
  historicalData: HistoricalDataMsQoL54[];
  lineColor: string;
  title: string;
}

const LineChartMSQoL54Component: React.FC<LineChartComponentProps> = ({ historicalData, title }) => {
  // Función para formatear la fecha
  const formatDate = (date: string) => {
    const formattedDate = format(new Date(date), 'MMM-yy', { locale: es });
    return formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);
  };

  return (
    <div style={{ width: '100%' }}>
      <h5 style={{ textAlign: 'left', fontWeight: 'bold', marginLeft: '20px', marginBottom: '10px' }}>
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
          <Line type="monotone" dataKey="physicalHealth" stroke={"red"} strokeWidth={2} activeDot={{ r: 8 }} />
          <Line type="monotone" dataKey="mentalHealth" stroke={"blue"} strokeWidth={2} activeDot={{ r: 8 }} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default LineChartMSQoL54Component;
