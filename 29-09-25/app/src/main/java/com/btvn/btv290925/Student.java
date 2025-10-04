package com.btvn.btv290925;

public class Student {
    private int id;
    private String name;
    private int age;
    private String className;

    // Constructor mặc định
    public Student() {
    }

    // Constructor với tất cả tham số
    public Student(int id, String name, int age, String className) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.className = className;
    }

    // Constructor không có ID (dùng khi thêm mới)
    public Student(String name, int age, String className) {
        this.name = name;
        this.age = age;
        this.className = className;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", className='" + className + '\'' +
                '}';
    }
}
