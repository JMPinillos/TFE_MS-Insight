package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MySqlQuestionsAnswers {
    private int form_id;
    private int question_id;
    private int answer_id;
}
