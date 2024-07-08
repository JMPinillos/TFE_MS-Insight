package com.msinsight.models;

import lombok.Getter;

@Getter
public class FormsModels {

    private final int formId;
    private final int patientIdColumn;
    private final int completeAtColumn;
    private final int firstColumn;
    private final int lastColumn;
    private final int completeColumn;
    private final Integer tScoreColumn;
    private final Integer startAnswerId;
    private final Integer endAnswerId;

    // Constructor
    public FormsModels(int formId, int completeAtColumn, int firstColumn, int lastColumn, int completeColumn, int patientIdColumn, Integer tScoreColumn, Integer startAnswerId, Integer endAnswerId) {
        this.formId = formId;
        this.patientIdColumn = patientIdColumn;
        this.completeAtColumn = completeAtColumn;
        this.completeColumn = completeColumn;
        this.firstColumn = firstColumn;
        this.lastColumn = lastColumn;
        this.tScoreColumn = tScoreColumn;
        this.startAnswerId = startAnswerId;
        this.endAnswerId = endAnswerId;
    }

}