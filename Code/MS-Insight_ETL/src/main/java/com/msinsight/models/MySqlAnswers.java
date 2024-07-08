package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MySqlAnswers {
    private int answer_id;
    private String title;
    private String type;
    private double value;
}
