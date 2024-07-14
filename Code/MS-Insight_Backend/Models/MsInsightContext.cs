using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace MsInsightApi.Models;

public partial class MsInsightContext : DbContext
{
    public MsInsightContext()
    {
    }

    public MsInsightContext(DbContextOptions<MsInsightContext> options)
        : base(options)
    {
    }

    public virtual DbSet<ViewFormsScore> ViewFormsScores { get; set; }

    public virtual DbSet<ViewFssScore> ViewFssScores { get; set; }

    public virtual DbSet<ViewHaqScore> ViewHaqScores { get; set; }

    public virtual DbSet<ViewMedicalConsultation> ViewMedicalConsultations { get; set; }

    public virtual DbSet<ViewMsis29Score> ViewMsis29Scores { get; set; }

    public virtual DbSet<ViewMsqoL54PhysicalAndMentalScore> ViewMsqoL54PhysicalAndMentalScores { get; set; }

    public virtual DbSet<ViewNeuroQoLcogScore> ViewNeuroQoLcogScores { get; set; }

    public virtual DbSet<ViewNeuroQoLsfScore> ViewNeuroQoLsfScores { get; set; }

    public virtual DbSet<ViewPatient> ViewPatients { get; set; }

    public virtual DbSet<ViewPatientsDatum> ViewPatientsData { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<ViewFormsScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_Forms_Scores");

            entity.Property(e => e.FormId).HasColumnName("form_id");
            entity.Property(e => e.MaximumScore).HasColumnName("maximum_score");
            entity.Property(e => e.MinimumScore).HasColumnName("minimum_score");
            entity.Property(e => e.Title)
                .HasMaxLength(50)
                .HasColumnName("title");
        });

        modelBuilder.Entity<ViewFssScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_FSS_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewHaqScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_HAQ_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewMedicalConsultation>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_Medical_Consultation");

            entity.Property(e => e.CompletedAt)
                .HasColumnType("date")
                .HasColumnName("completed_at");
            entity.Property(e => e.FormId).HasColumnName("form_id");
            entity.Property(e => e.PatientId).HasColumnName("patient_id");
            entity.Property(e => e.Title)
                .HasMaxLength(250)
                .HasColumnName("title");
            entity.Property(e => e.Value)
                .HasColumnType("mediumtext")
                .HasColumnName("value");
        });

        modelBuilder.Entity<ViewMsis29Score>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_MSIS29_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewMsqoL54PhysicalAndMentalScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_MSQoL54_Physical_And_Mental_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.SaludFisica).HasColumnName("Salud_Fisica");
            entity.Property(e => e.SaludMental).HasColumnName("Salud_Mental");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewNeuroQoLcogScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_NeuroQoLCOG_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewNeuroQoLsfScore>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_NeuroQoLSF_Scores");

            entity.Property(e => e.Completado).HasColumnType("date");
            entity.Property(e => e.NumeroPaciente).HasColumnName("Numero_paciente");
            entity.Property(e => e.Test).HasMaxLength(50);
        });

        modelBuilder.Entity<ViewPatient>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_Patients");

            entity.Property(e => e.PatientId).HasColumnName("patient_id");
        });

        modelBuilder.Entity<ViewPatientsDatum>(entity =>
        {
            entity
                .HasNoKey()
                .ToView("View_Patients_Data");

            entity.Property(e => e.BirthDate)
                .HasColumnType("date")
                .HasColumnName("birth_date");
            entity.Property(e => e.BrainMri)
                .HasMaxLength(50)
                .HasColumnName("brain_MRI");
            entity.Property(e => e.Gender)
                .HasMaxLength(50)
                .HasColumnName("gender");
            entity.Property(e => e.OnsetSymptoms)
                .HasColumnType("date")
                .HasColumnName("onset_symptoms");
            entity.Property(e => e.PatientId).HasColumnName("patient_id");
            entity.Property(e => e.SpinalMri)
                .HasMaxLength(50)
                .HasColumnName("spinal_MRI");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
