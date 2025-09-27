package com.ensak.emploi.model;

public class ProfProgram {
    private String programName;
    private Long programId;

    public String getProgramName() {
        return programName;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}