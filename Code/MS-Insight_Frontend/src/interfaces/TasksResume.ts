// Define la interfaz TasksResume para representar un resumen de tareas
export interface TasksResume {
    total: number; // Propiedad 'total': número total de tareas
    completes: number; // Propiedad 'completes': número de tareas que están completadas
    pending: number; // Propiedad 'pending': número de tareas que están pendientes de completar
}