package com.mycompany.app;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstname() {
        return firstName;
    }
    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName (String lastName) {
        this.lastName = lastName;
    }
    @Override
    public String toString() {
        return "\n"+ this.id+"\n" + this.firstName + "\n" + this.lastName + "\n";
    }
    
}