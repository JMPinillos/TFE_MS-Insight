package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MySqlForms {
    private int form_id;
    private String title;
    private String description;
    private String formula;
    private double minimum_score;
    private double maximum_score;
}
