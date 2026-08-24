
package com.mycompany.poepart1;


public class Patient {
    private String patientID;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private PatientCategory patientCategory;

    //constructor
    public Patient(String patientID, String firstName, String lastName, int age, String gender, String medicalCondition,PatientCategory patientCategory) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.patientCategory = patientCategory;
    }

    //getters
    public String getPatientID() {
        return patientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getMedicalCondItion() {
        return medicalCondition;
    }

    public PatientCategory getPatientCategory() {
        return patientCategory;
    }

    //setters
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public void setPatientCategory( PatientCategory patientCategory) {
        this.patientCategory = patientCategory;
    }
    
    //print table
    public void DisplayPateintDetails(){
            System.out.println("Patient ID:"+patientID);
            System.out.println("irst name:"+firstName);
            System.out.println("Last Name:"+lastName);
            System.out.println("Age:"+age);
            System.out.println("Gender:"+gender);
            System.out.println("Medical Condition:"+ medicalCondition);
            System.out.println("Category:"+ patientCategory);
    }
}
