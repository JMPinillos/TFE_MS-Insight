package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class MySqlPatients {
    private int patient_id;
    private int gender_id;
    private Date birth_date;
    private Date onset_symptoms;
    private int brain_MRI_id;
    private int spinal_MRI_id;
}
