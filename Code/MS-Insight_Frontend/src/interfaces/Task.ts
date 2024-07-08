// Define la interfaz Task para representar una tarea
export interface Task {
  id: string; // Propiedad 'id': identificador único de la tarea
  userIdCreator: string; // Propiedad 'userIdCreator': identificador del usuario que creó la tarea
  title: string; // Propiedad 'title': título de la tarea
  description: string; // Propiedad 'description': descripción detallada de la tarea
  createdAt: Date; // Propiedad 'createdAt': fecha y hora de creación de la tarea
  finishAt?: Date | null; // Propiedad 'finishAt': fecha y hora de finalización de la tarea. Es opcional y puede ser Date, null, o estar ausente
  userIdFinished?: string | null; // Propiedad 'userIdFinished': identificador del usuario que completó la tarea. Es opcional y puede ser string, null, o estar ausente
  isCompleted: boolean; // Propiedad 'isCompleted': indica si la tarea está completada
}
