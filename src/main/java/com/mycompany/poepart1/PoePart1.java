

package com.mycompany.poepart1;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PoePart1 {

        private static final Scanner scanner = new Scanner(System.in);
        private static final Hosptal hospital = new Hosptal();
    
    public static void main(String[] args) {
        

        boolean running = true;

        System.out.println("========================================");
        System.out.println("     MEDICARE HOSPITAL ADMISSION");
        System.out.println("========================================");

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        registerPatient();
                        break;

                    case 2:
                        searchPatient();
                        break;

                    case 3:
                        updatePatient();
                        break;

                    case 4:
                        deletePatient();
                        break;

                    case 5:
                        hospital.displayAllPatients();
                        break;

                    case 6:
                        allocateBed();
                        break;

                    case 7:
                        releaseBed();
                        break;

                    case 8:
                        hospital.displayWardLayout();
                        break;

                    case 9:
                        hospital.displayAvailableBeds();
                        break;

                    case 10:
                        hospital.displayOccupiedBeds();
                        break;

                    case 11:
                        hospital.generateReport();
                        break;

                    case 12:
                        hospital.displayPatientsSortedBySurname();
                        break;

                    case 13:
                        hospital.displayPatientsSortedById();
                        break;

                    case 0:
                        running = false;
                        System.out.println(
                                "\nThank you for using MediCare Hospital System."
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid option. Please choose from the menu."
                        );
                }

            } catch (IllegalArgumentException | IllegalStateException e) {

                System.out.println("\nERROR: " + e.getMessage());
            }
        }

        
    }

    // MENU
   private static void displayMenu() {

        System.out.println("\n========================================");
        System.out.println("              Main menu");
        System.out.println("========================================");

        System.out.println("1.  Register Patient");
        System.out.println("2.  Search Patient");
        System.out.println("3.  Update Patient");
        System.out.println("4.  Delete Patient");
        System.out.println("5.  Display All Patients");
        System.out.println("========================================");
        System.out.println("6.  Allocate Bed");
        System.out.println("7.  Release Bed");
        System.out.println("8.  Display Ward Layout");
        System.out.println("9.  Display Available Beds");
        System.out.println("10. Display Occupied Beds");
        System.out.println("========================================");
        System.out.println("11. Generate Ward Report");
        System.out.println("12. Sort Patients by Surname");
        System.out.println("13. Sort Patients by Patient ID");

        System.out.println("-----------------------------------------");

        System.out.println("0.  Exit");

        System.out.println("========================================");
    }


    // PATIENT REGISTRATION
   private static void registerPatient() {

        System.out.println("\n========== Rrgister patient ==========");

        String patientId = readString("Patient ID: ");

        String firstName = readString("First Name: ");

        String lastName = readString("Last Name: ");

        int age = readInt("Age: ");

        String gender = readString("Gender: ");

        String condition = readString("Medical Condition: ");

        System.out.println("\nPatient Category:");

        System.out.println("1.Inpatient");
        System.out.println("2.Outpatient");
        System.out.println("3.Emergency");

        int categoryChoice = readInt("Choose category: ");

        Patient patient;

        switch (categoryChoice) {

            case 1:

                // Inpatient initially has no bed
                patient = new Inpatient(patientId,firstName,lastName,age,gender,condition,1,"NONE");
                break;

            case 2:

                patient = new Patient( patientId,firstName,lastName, age, gender, condition,  PatientCategory.Outpatient );
                break;

            case 3:

                patient = new Patient( patientId, firstName, lastName, age, gender, condition, PatientCategory.Emergency);
                break;

            default:

                System.out.println("Invalid category.");
                return;
        }

        if (hospital.registerPatient(patient)) {
            System.out.println( "\nPatient registered successfully." );
        }
    }


    // SEARCH
   private static void searchPatient() {

        System.out.println("\n========== SEARCH PATIENT ==========");

        String patientId = readString("Enter Patient ID: ");

        Patient patient = hospital.searchPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
        } else {

            System.out.println("\nPatient found:");

            System.out.println("=============================");

            patient.DisplayPateintDetails();

            System.out.println("==============================");
        }
    }


    // UPDATE
    private static void updatePatient() {

        System.out.println("\n========== Update Patient ==========");

        String patientId = readString("Enter Patient ID: ");

        Patient patient = hospital.searchPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println( "\nEnter the patient's new information:" );
        String firstName = readString("First Name: ");
        String lastName = readString("Last Name: ");
        int age = readInt("Age: ");
        String gender = readString("Gender: ");
        String condition = readString( "Medical Condition: " );

        boolean updated = hospital.updatePatient(patientId, firstName,lastName, age, gender, condition);

        if (updated) {
            System.out.println( "Patient updated successfully.");
        }
    }


    // DELETE
    private static void deletePatient() {

        System.out.println("\n========== DELETE PATIENT ==========");
        String patientId = readString("Enter Patient ID: ");
        Patient patient = hospital.searchPatient(patientId);
        if (patient == null) {

            System.out.println("Patient not found.");
            return;
        }

        boolean deleted = hospital.deletePatient(patientId);

        if (deleted) {
            System.out.println( "Patient deleted successfully." );
        }
    }

    // BED ALLOCATION
    private static void allocateBed() {

        System.out.println("\n========== ALLOCATE BED ==========");
        String patientId = readString("Enter Inpatient ID: " );
        Patient patient = hospital.searchPatient(patientId);

        if (patient == null) {

            System.out.println("Patient not found.");
            return;
        }

        if (!(patient instanceof Inpatient)) {
            System.out.println("Only Inpatients can be allocated beds.");
            return;
        }

        System.out.println("\nAvailable beds:");

        hospital.displayAvailableBeds();

        System.out.println("\nChoose allocation method:");

        System.out.println("1. Choose a specific bed");
        System.out.println("2. Automatically assign first available bed");

        int choice = readInt("Enter choice: ");

        if (choice == 1) {

            String bedNumber = readString("Enter bed number (B01-B20): ");

            if (hospital.allocateBed(patientId, bedNumber)) {
                System.out.println( "Bed " + bedNumber + " successfully allocated." );
            }

        } else if (choice == 2) {

            String bedNumber =hospital.allocateFirstAvailableBed(patientId);
            System.out.println( "Bed " + bedNumber + " successfully allocated." );

        } else {
            System.out.println("Invalid choice.");
        }
    }

 
    // BED RELEASE
    private static void releaseBed() {

        System.out.println("\n========== RELEASE BED ==========");

        String patientId = readString("Enter Patient ID:" );

        Patient patient = hospital.searchPatient(patientId);

        if (patient == null) {

            System.out.println("Patient not found.");
            return;
        }

        boolean released = hospital.releaseBed(patientId);

        if (released) {
            System.out.println( "Bed released successfully." );

        } else {
            System.out.println( "This patient does not currently occupy a bed." );
        }
    }

 
    // INPUT METHODS / EXCEPTION HANDLING
   private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                int value = scanner.nextInt();

                scanner.nextLine();

                return value;

            } catch (InputMismatchException e) {

                System.out.println( "Invalid input. Please enter a number.");

                scanner.nextLine();
            }
        }
    }

    private static String readString(String message) {

        while (true) {

            System.out.print(message);

            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println( "This field cannot be empty.");
        }
    }
}
