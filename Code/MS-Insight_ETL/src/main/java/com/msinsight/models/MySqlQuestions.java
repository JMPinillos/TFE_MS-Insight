package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MySqlQuestions {
    private int question_id;
    private int number;
    private String title;
}
