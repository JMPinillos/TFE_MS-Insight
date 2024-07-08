package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class MySqlPatientsForms {
    private int patient_id;
    private int form_id;
    private Date completed_at;
    private int patient_form_id;
    private double score;
}
