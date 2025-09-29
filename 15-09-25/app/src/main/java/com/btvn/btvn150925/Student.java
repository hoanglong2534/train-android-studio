package com.btvn.btvn150925;

public class Student {
    private String name;
    private String id;
    private String className;

    public Student(String name, String id, String className) {
        this.name = name;
        this.id = id;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }
}
