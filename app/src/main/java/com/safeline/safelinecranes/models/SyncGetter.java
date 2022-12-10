package com.safeline.safelinecranes.models;

import java.util.List;

public class SyncGetter {
    private List<Position> positions;
    private List<RopeType> ropetypes;
    private List<Rope> ropes;
    private List<Inspection> inspections;
    private List<FinishedResults> finishedResults;
    private List<Result> results;

    public SyncGetter() {
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Rope> getRopes() {
        return ropes;
    }

    public void setRopes(List<Rope> ropes) {
        this.ropes = ropes;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public List<RopeType> getRopetypes() {
        return ropetypes;
    }

    public void setRopetypes(List<RopeType> ropetypes) {
        this.ropetypes = ropetypes;
    }

    public List<FinishedResults> getFinishedResults() {
        return finishedResults;
    }

    public void setFinishedResults(List<FinishedResults> finishedResults) {
        this.finishedResults = finishedResults;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
