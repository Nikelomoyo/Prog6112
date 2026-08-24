package com.mycompany.poepart1;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class HospitalTest {
    
  

    // =========================================================
    // TEST 1: REGISTER A PATIENT
    // =========================================================

    @Test
    public void testRegisterPatient() {

        Hosptal hospital = new Hosptal();

        Patient patient = new Patient( "P001", "John", "Smith",  25, "Male", "Flu", PatientCategory.Outpatient);

        boolean result = hospital.registerPatient(patient);

        assertTrue(result);

        assertEquals(
                1,
                hospital.getTotalPatients()
        );
    }

    // =========================================================
    // TEST 2: SEARCH FOR A PATIENT
    // =========================================================

    @Test
    public void testSearchPatient() {

        Hosptal hospital = new Hosptal();

        Patient patient = new Patient("P001","John","Smith",25,"Male","Flu",PatientCategory.Outpatient
        );

        hospital.registerPatient(patient);

        Patient found = hospital.searchPatient("P001");

        assertNotNull(found);

        assertEquals("John",found.getFirstName());

        assertEquals("Smith",found.getLastName());
    }

    // =========================================================
    // TEST 3: UPDATE PATIENT
    // =========================================================

    @Test
    public void testUpdatePatient() {

        Hosptal hospital = new Hosptal();

        Patient patient = new Patient("P001","John","Smith",25,"Male","Flu",PatientCategory.Outpatient);

        hospital.registerPatient(patient);

        boolean result = hospital.updatePatient( "P001","James", "Smith",30,"Male","Pneumonia" );

        assertTrue(result);

        Patient updated = hospital.searchPatient("P001");

        assertEquals("James",updated.getFirstName());

        assertEquals(30,updated.getAge());

        assertEquals("Pneumonia",updated.getMedicalCondItion()
        );
    }

    // =========================================================
    // TEST 4: DELETE PATIENT
    // =========================================================

    @Test
    public void testDeletePatient() {

        Hosptal hospital = new Hosptal();

        Patient patient = new Patient( "P001","John", "Smith",25, "Male", "Flu",PatientCategory.Outpatient);

        hospital.registerPatient(patient);

        assertEquals( 1, hospital.getTotalPatients());

        boolean result = hospital.deletePatient("P001");

        assertTrue(result);

        assertEquals( 0, hospital.getTotalPatients() );

        assertNull(
                hospital.searchPatient("P001")
        );
    }

    // =========================================================
    // TEST 5: ALLOCATE A BED
    // =========================================================

    @Test
    public void testAllocateBed() {

        Hosptal hospital = new Hosptal();

        Inpatient patient = new Inpatient( "P001","John","Smith",  25,"Male","Flu", 1, "NONE");

        hospital.registerPatient(patient);

        boolean result = hospital.allocateBed("P001", "B01");

        assertTrue(result);

        assertEquals( "B01", patient.getBedNumber() );

        assertEquals(1, hospital.getOccupiedBeds());
    }

    // =========================================================
    // TEST 6: RELEASE A BED
    // =========================================================

    @Test
    public void testReleaseBed() {

        Hosptal hospital = new Hosptal();

        Inpatient patient = new Inpatient( "P001", "John","Smith",25,"Male","Flu", 1,"NONE");

        hospital.registerPatient(patient);

        hospital.allocateBed( "P001", "B01" );

        assertEquals( 1, hospital.getOccupiedBeds());

        boolean result =hospital.releaseBed("P001");

        assertTrue(result);

        assertEquals( 0, hospital.getOccupiedBeds());

        assertEquals( "NONE", patient.getBedNumber());
    }

    // =========================================================
    // TEST 7: PREVENT DUPLICATE PATIENT IDs
    // =========================================================

    @Test
    public void testPreventDuplicatePatientIds() {

        Hosptal hospital = new Hosptal();

        Patient patient1 = new Patient( "P001", "John", "Smith",25,"Male","Flu",PatientCategory.Outpatient);

        Patient patient2 = new Patient("P001", "James", "Jones",30, "Male", "Cold",PatientCategory.Emergency);

        hospital.registerPatient(patient1);

        assertThrows(IllegalArgumentException.class,() -> hospital.registerPatient(patient2) );
    }

    // =========================================================
    // TEST 8: PREVENT ALLOCATING OCCUPIED BED
    // =========================================================

    @Test
    public void testPreventOccupiedBedAllocation() {

        Hosptal hospital = new Hosptal();

        Inpatient patient1 = new Inpatient("P001","John","Smith",25, "Male","Flu",1,"NONE");

        Inpatient patient2 = new Inpatient("P002","James","Jones",30,"Male","Cold",1,"NONE");

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);

        hospital.allocateBed("P001","B01");

        assertThrows(
                IllegalStateException.class,
                () -> hospital.allocateBed("P002",  "B01")
        );
    }

    // =========================================================
    // TEST 9: PREVENT BED ALLOCATION TO OUTPATIENT
    // =========================================================

    @Test
    public void testOutpatientCannotReceiveBed() {

        Hosptal hospital = new Hosptal();

        Patient patient = new Patient("P001","John","Smith",25,"Male", "Flu",PatientCategory.Outpatient);

        hospital.registerPatient(patient);

        assertThrows(
                IllegalArgumentException.class,
                () -> hospital.allocateBed("P001","B01")
        );
    }

    // =========================================================
    // TEST 10: PREVENT BED ALLOCATION WHEN FULL
    // =========================================================

    @Test
    public void testPreventAllocationWhenBedsAreFull() {

        Hosptal hospital = new Hosptal();

        // Fill all 20 beds
        for (int i = 1; i <= 20; i++) {

            String patientId =
                    String.format("P%03d", i);

            Inpatient patient = new Inpatient(patientId,"First" + i, "Last" + i,20 + i, "Male", "Condition",1,  "NONE");

            hospital.registerPatient(patient);

            String bedNumber =
                    String.format("B%02d", i);

            hospital.allocateBed(patientId,bedNumber);
        }

        assertEquals(
                20,
                hospital.getOccupiedBeds()
        );

        // Add another inpatient
        Inpatient extraPatient = new Inpatient("P021","Extra","Patient",30, "Female","Condition",1, "NONE" );

        hospital.registerPatient(extraPatient);

        assertThrows(
                IllegalStateException.class,
                () -> hospital.allocateFirstAvailableBed( "P021")
        );
    }

    // =========================================================
    // TEST 11: SORT PATIENTS BY SURNAME
    // =========================================================

    @Test
    public void testSortPatientsBySurname() {

        Hosptal hospital = new Hosptal();

        Patient patient1 = new Patient("P001","John","Zulu",25, "Male","Flu",PatientCategory.Outpatient);

        Patient patient2 = new Patient("P002","James","Adams",30,"Male","Cold", PatientCategory.Outpatient);

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);

        hospital.sortBySurname();

        assertEquals("Adams", hospital.searchPatient("P002").getLastName()
        );
    }

    // =========================================================
    // TEST 12: SORT PATIENTS BY ID
    // =========================================================

    @Test
    public void testSortPatientsById() {

        Hosptal hospital = new Hosptal();

        Patient patient1 = new Patient("P002","John","Smith", 25, "Male","Flu",PatientCategory.Outpatient);

        Patient patient2 = new Patient("P001","James","Jones",30, "Male","Cold", PatientCategory.Outpatient);

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);

        hospital.sortByPatientId();

        assertEquals("P001",hospital.searchPatient("P001").getPatientID());
    }

    // =========================================================
    // TEST 13: OCCUPANCY PERCENTAGE
    // =========================================================

    @Test
    public void testOccupancyPercentage() {

        Hosptal hospital = new Hosptal();

        Inpatient patient = new Inpatient( "P001","John","Smith", 25, "Male", "Flu", 1, "NONE" );

        hospital.registerPatient(patient);

        hospital.allocateBed("P001","B01");

        assertEquals( 5.0,hospital.getOccupancyPercentage(),0.001);
    }
}
