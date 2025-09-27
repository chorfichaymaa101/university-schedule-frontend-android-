package com.ensak.emploi.model;

import java.util.List;

public class Prof {
    private String name;
    private String email;
    private final String role = "PROF";
    public List<String> programs ;

    public List<String> getPrograms() {
        return programs;
    }

    public void setPrograms(List<String> programs) {
        this.programs = programs;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public Prof(String name) {
        this.name = name;
    }

    public Prof() {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
