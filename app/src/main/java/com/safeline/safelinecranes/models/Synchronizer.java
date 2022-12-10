package com.safeline.safelinecranes.models;

import java.util.List;

public class Synchronizer {
    private String username;
    private List<Position> positions;
    private List<RopeType> ropeTypes;
    private List<Rope> ropes;
    private List<Inspection> inspections;
    private List<FinishedResults> results;
    private List<Result> detailed_results;
    private List<DeletedObject> deletedObjects;

    public Synchronizer() { }

    public List<Result> getDetailed_results() {
        return detailed_results;
    }

    public void setDetailed_results(List<Result> detailed_results) {
        this.detailed_results = detailed_results;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<RopeType> getRopeTypes() {
        return ropeTypes;
    }

    public void setRopeTypes(List<RopeType> ropeTypes) {
        this.ropeTypes = ropeTypes;
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

    public List<FinishedResults> getResults() {
        return results;
    }

    public void setResults(List<FinishedResults> results) {
        this.results = results;
    }

    public List<DeletedObject> getDeletedObjects() {
        return deletedObjects;
    }

    public void setDeletedObjects(List<DeletedObject> deletedObjects) {
        this.deletedObjects = deletedObjects;
    }
}
