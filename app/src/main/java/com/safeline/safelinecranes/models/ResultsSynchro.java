package com.safeline.safelinecranes.models;

import java.util.List;

public class ResultsSynchro {

    private List<Inspection> inspections;
    private List<FinishedResults> fResults;

    public ResultsSynchro() {
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public List<FinishedResults> getfResults() {
        return fResults;
    }

    public void setfResults(List<FinishedResults> fResults) {
        this.fResults = fResults;
    }
}
