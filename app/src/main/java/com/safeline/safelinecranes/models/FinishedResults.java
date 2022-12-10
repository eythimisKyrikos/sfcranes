package com.safeline.safelinecranes.models;

public class FinishedResults {
    public int id;
    public int inspection_id;
    public boolean sync;
    public String dateOfInspection;
    public String results;

    public FinishedResults() { }

    public FinishedResults(int id, boolean sync, String dateOfInspection, String results, int inspection_id) {
        this.id = id;
        this.sync = sync;
        this.dateOfInspection = dateOfInspection;
        this.results = results;
        this.inspection_id = inspection_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getDateOfInspection() {
        return dateOfInspection;
    }

    public void setDateOfInspection(String dateOfInspection) {
        this.dateOfInspection = dateOfInspection;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public int getInspection_id() {
        return inspection_id;
    }

    public void setInspection_id(int inspection_id) {
        this.inspection_id = inspection_id;
    }
}
