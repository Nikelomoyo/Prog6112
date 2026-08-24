
package com.mycompany.poepart1;

import java.util.ArrayList;
import java.util.Comparator;

public class Hosptal {

    // ArrayList stores all registered patientss
    private ArrayList<Patient> patients;

    // 4 x 5 two-dimensional array representing the 20 beds
    private Patient[][] beds;

    private static final int ROWS = 4;
    private static final int COLUMNS = 5;

    // Constructor
    public Hosptal() {
        patients = new ArrayList<>();
        beds = new Patient[ROWS][COLUMNS];
    }

    // =========================================================
    // FEATURE 1: PATIENT MANAGEMENT
    // =========================================================

    // Register a patient
    public boolean registerPatient(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        // Prevent duplicate Patient IDs
        if (searchPatient(patient.getPatientID()) != null) {
            throw new IllegalArgumentException( "A patient with ID " + patient.getPatientID()+ " already exists.");
        }

        patients.add(patient);
        return true;
    }

    // Search for a patient using Patient ID
    public Patient searchPatient(String patientID) {

        for (Patient patient : patients) {

            if (patient.getPatientID().equalsIgnoreCase(patientID)) {
                return patient;
            }
        }

        return null;
    }

    // Update patient details
    public boolean updatePatient(String patientId,String firstName,String lastName,int age,String gender,String medicalCondition) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);

        return true;
    }

    // Delete a patient
    public boolean deletePatient(String patientId) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        // If the patient is an inpatient, release their bed first
        if (patient instanceof Inpatient) {
            releaseBed(patientId);
        }

        patients.remove(patient);

        return true;
    }

    // Display all patients
    public void displayAllPatients() {

        if (patients.isEmpty()) {
            System.out.println("No patients are currently registered.");
            return;
        }

        System.out.println("\n========== ALL PATIENTS ==========");

        for (Patient patient : patients) {

            System.out.println("----------------------------------");

            patient.DisplayPateintDetails();
        }

        System.out.println("----------------------------------");
    }

    // Get number of registered patients
    public int getTotalPatients() {
        return patients.size();
    }

    // =========================================================
    // FEATURE 2: BED MANAGEMENT
    // =========================================================

    // Convert bed number such as B01 into array row/column
    private int[] getBedPosition(String bedNumber) {

        if (bedNumber == null) {
            return null;
        }

        bedNumber = bedNumber.toUpperCase();

        if (!bedNumber.matches("B(0[1-9]|1[0-9]|20)")) {
            return null;
        }

        int bedNumberValue;

        try {
            bedNumberValue = Integer.parseInt(
                    bedNumber.substring(1)
            );
        } catch (NumberFormatException e) {
            return null;
        }

        int index = bedNumberValue - 1;

        int row = index / COLUMNS;
        int column = index % COLUMNS;

        return new int[]{row, column};
    }

    // Get bed number from row and column
    private String getBedNumber(int row, int column) {

        int number = (row * COLUMNS) + column + 1;

        return String.format("B%02d", number);
    }

    // Check if a bed is occupied
    public boolean isBedOccupied(String bedNumber) {

        int[] position = getBedPosition(bedNumber);

        if (position == null) {
            throw new IllegalArgumentException("Invalid bed number.");
        }

        return beds[position[0]][position[1]] != null;
    }

    // Allocate a specific bed
    public boolean allocateBed(String patientId, String bedNumber) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            throw new IllegalArgumentException( "Patient does not exist.");
        }

        // Only Inpatients can receive beds
        if (!(patient instanceof Inpatient)) {
            throw new IllegalArgumentException("Only Inpatients may be allocated a hospital bed.");
        }

        Inpatient inpatient = (Inpatient) patient;

        // Prevent one inpatient from occupying two beds
        if (inpatient.getBedNumber() != null& !inpatient.getBedNumber().equalsIgnoreCase("NONE")) {

            throw new IllegalArgumentException("This inpatient already has a bed.");
        }

        int[] position = getBedPosition(bedNumber);

        if (position == null) {
            throw new IllegalArgumentException("Invalid bed number. Use B01 to B20.");
        }

        // Prevent allocating an occupied bed
        if (beds[position[0]][position[1]] != null) {
            throw new IllegalStateException( "Bed " + bedNumber + " is already occupied.");
        }

        beds[position[0]][position[1]] = patient;

        inpatient.setBedNumber(bedNumber.toUpperCase());

        return true;
    }

    // Automatically allocate the first available bed
    public String allocateFirstAvailableBed(String patientId) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            throw new IllegalArgumentException("Patient does not exist.");
        }

        if (!(patient instanceof Inpatient)) {
            throw new IllegalArgumentException("Only Inpatients may be allocated a bed.");
        }

        Inpatient inpatient = (Inpatient) patient;

        if (inpatient.getBedNumber() != null
                && !inpatient.getBedNumber().equalsIgnoreCase("NONE")) {

            throw new IllegalArgumentException("This inpatient already has a bed.");
        }

        // Nested loops through the 2D array
        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                if (beds[row][column] == null) {

                    beds[row][column] = patient;

                    String bedNumber = getBedNumber(row, column);

                    inpatient.setBedNumber(bedNumber);

                    return bedNumber;
                }
            }
        }

        // No available beds
        throw new IllegalStateException("No beds are currently available.");
    }

    // Release a bed using patient ID
    public boolean releaseBed(String patientId) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        // Search through all beds
        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                if (beds[row][column] == patient) {

                    beds[row][column] = null;

                    if (patient instanceof Inpatient) {
                        Inpatient inpatient = (Inpatient) patient;
                        inpatient.setBedNumber("NONE");
                    }

                    return true;
                }
            }
        }

        return false;
    }

    // Release a specific bed
    public boolean releaseSpecificBed(String bedNumber) {

        int[] position = getBedPosition(bedNumber);

        if (position == null) {
            throw new IllegalArgumentException("Invalid bed number.");
        }

        Patient patient = beds[position[0]][position[1]];

        if (patient == null) {
            return false;
        }

        beds[position[0]][position[1]] = null;

        if (patient instanceof Inpatient) {
            Inpatient inpatient = (Inpatient) patient;
            inpatient.setBedNumber("NONE");
        }

        return true;
    }

    // Display complete ward layout
    public void displayWardLayout() {

        System.out.println("\n========== WARD LAYOUT ==========");

        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                String bedNumber = getBedNumber(row, column);

                if (beds[row][column] == null) {
                    System.out.printf("[%s: Available] ", bedNumber);
                } else {
                    System.out.printf(
                            "[%s: %s] ", bedNumber,beds[row][column].getPatientID());
                }
            }

            System.out.println();
        }
    }

    // Display available beds
    public void displayAvailableBeds() {

        System.out.println("\n========== AVAILABLE BEDS ==========");

        boolean found = false;

        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                if (beds[row][column] == null) {

                    System.out.print(
                            getBedNumber(row, column) + " ");

                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No beds are available.");
        } else {
            System.out.println();
        }
    }

    // Display occupied beds
    public void displayOccupiedBeds() {

        System.out.println("\n========== OCCUPIED BEDS ==========");

        boolean found = false;

        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                if (beds[row][column] != null) {

                    Patient patient = beds[row][column];

                    System.out.println(
                            getBedNumber(row, column)+ " -> "+ patient.getPatientID()+ " - "+ patient.getFirstName()+ " "+ patient.getLastName()
                    );

                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No beds are occupied.");
        }
    }

    // Count occupied beds
    public int getOccupiedBeds() {

        int count = 0;

        for (int row = 0; row < beds.length; row++) {

            for (int column = 0; column < beds[row].length; column++) {

                if (beds[row][column] != null) {
                    count++;
                }
            }
        }

        return count;
    }

    // Count available beds
    public int getAvailableBeds() {
        return (ROWS * COLUMNS) - getOccupiedBeds();
    }

    // =========================================================
    // FEATURE 3: REPORTS
    // =========================================================

    public double getOccupancyPercentage() {

        int totalBeds = ROWS * COLUMNS;

        return ((double) getOccupiedBeds() / totalBeds) * 100;
    }

    public void generateReport() {

        System.out.println("\n========================================");
        System.out.println("          MEDICARE WARD REPORT");
        System.out.println("========================================");

        System.out.println("Total registered patients: "+ getTotalPatients());

        System.out.println(
                "Total occupied beds      : "
                + getOccupiedBeds()
        );

        System.out.println("Total available beds: "+ getAvailableBeds());

        System.out.printf("Ward occupancy: %.2f%%%n",getOccupancyPercentage());

        System.out.println("========================================");
    }

    // =========================================================
    // SORTING
    // =========================================================

    // Sort patients by surname
    public void sortBySurname() {

        patients.sort(
                Comparator.comparing(
                        Patient::getLastName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );
    }

    // Sort patients by Patient ID
    public void sortByPatientId() {

        patients.sort(
                Comparator.comparing(
                        Patient::getPatientID,
                        String.CASE_INSENSITIVE_ORDER
                )
        );
    }

    // Display sorted patients by surname
    public void displayPatientsSortedBySurname() {

        sortBySurname();

        System.out.println("\n===== PATIENTS SORTED BY SURNAME =====");

        displayAllPatients();
    }

    // Display sorted patients by ID
    public void displayPatientsSortedById() {

        sortByPatientId();

        System.out.println("\n===== PATIENTS SORTED BY ID =====");

        displayAllPatients();
    }
}
