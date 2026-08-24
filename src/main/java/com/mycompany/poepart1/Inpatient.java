
package com.mycompany.poepart1;

public class Inpatient extends Patient{
    private int wardNumber;
    private String bedNumber;
    
    //inherited constructor
    public Inpatient(String patientID, String firstName, String lastName, int age, String gender, String medicalCondition, int wardNumber, String bedNumber){
        super(patientID, firstName, lastName, age, gender,medicalCondition, PatientCategory.INPATIENT);

        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }
    
    //getters

    public int getWardNumber() {
        return wardNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    //setters
    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }
    
    //overrided method
    @Override
    public void DisplayPateintDetails(){
        super.DisplayPateintDetails();
            System.out.println("Ward number:"+wardNumber);
            System.out.println("Bed number:"+bedNumber);
    }
}
