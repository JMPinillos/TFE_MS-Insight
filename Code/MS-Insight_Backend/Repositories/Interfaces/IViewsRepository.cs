using MsInsightApi.Models;

namespace MsInsightApi.Repositories.Interfaces;

public interface IViewsRepository
{
    Task<IEnumerable<ViewFormsScore>> GetFormsScores();
    Task<IEnumerable<ViewPatient>> GetPatients();
    Task<IEnumerable<ViewPatientsDatum>> GetPatientData(int patientId);
    Task<IEnumerable<ViewFssScore>> GetFssScores(int patientId);
    Task<IEnumerable<ViewHaqScore>> GetHaqScores(int patientId);
    Task<IEnumerable<ViewMsis29Score>> GetMsis29ScoresByPatient(int patientId);
    Task<IEnumerable<ViewMsqoL54PhysicalAndMentalScore>> GetMsqoL54PhysicalAndMentalScores(int patientId);
    Task<IEnumerable<ViewNeuroQoLsfScore>> GetNeuroQoLsfScores(int patientId);
    Task<IEnumerable<ViewNeuroQoLcogScore>> GetNeuroQoLcogScores(int patientId);
    Task<IEnumerable<ViewMedicalConsultation>> GetMedicalConsultations(int patientId);
}
