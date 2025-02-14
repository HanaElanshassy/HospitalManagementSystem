import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Patient {

    private static int idCounter = 1;
    private static PatientBST patientTree = new PatientBST();

    private int patientID;
    private String name;
    private int age;
    private String contactInfo;
    private String medicalHistory;
    private List<String> visitRecords;

    public List<Appointment> appointments;
private Billing billing;
    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", contactInfo='" + contactInfo + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }

    public Patient() {
    }


    public Patient(String name, int age, String contactInfo, String medicalHistory) {
        this.patientID = generateUniqueID();
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
        this.medicalHistory = medicalHistory;
        this.visitRecords = new ArrayList<>();
        this.appointments = new ArrayList<>();
        patientTree.insert(this);
        System.out.println("Patient added successfully with ID: " + this.patientID);
        this.billing = new Billing(patientID, this);
    }

    public int getAge() {
        return age;
    }
    public Billing getBilling() {
        return billing;
    }

    private int generateUniqueID() {
        int uniqueID;
        do {
            int randomNum = new Random().nextInt(900) + 100;
            uniqueID =  randomNum;
        } while (isIDExists(uniqueID));
        return uniqueID;
    }
    public void displayAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for Patient ID: " + patientID);
        } else {
            System.out.println("Appointments for Patient ID: " + patientID);
            for (Appointment appointment : appointments) {
                System.out.println(appointment.getAppointmentDetails());
                System.out.println("-----------------------------------");
            }
        }
    }


    private boolean isIDExists(int id) {
        return patientTree.search(id) != null;
    }

    public int getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public void updateContactInfo(String newContactInfo) {
        this.contactInfo = newContactInfo;
        System.out.println("Contact info updated for Patient ID: " + patientID);
    }
    public void updateVisitRecord(String visitDate, String newStatus) {
        boolean recordFound = false;

        for (int i = 0; i < this.visitRecords.size(); i++) {
            String record = this.visitRecords.get(i);


            if (record.startsWith("Date: " + visitDate)) {
                this.visitRecords.set(i, "Date: " + visitDate + ", Status: " + newStatus);
                System.out.println("Visit record updated: Date: " + visitDate + ", Status: " + newStatus);
                recordFound = true;
                break;
            }
        }

        if (!recordFound) {
            System.out.println("No visit record found for the specified date: " + visitDate);
        }
    }


    public void addVisitRecord(String visitRecord) {
        this.visitRecords.add(visitRecord);
        System.out.println("Visit record added: " + visitRecord);
    }
    public String getPatientInfo() {
        return "Patient ID: " + patientID + "\nName: " + name + "\nAge: " + age +
                "\nContact Info: " + contactInfo + "\nMedical History: " + medicalHistory +
                "\nVisit Records: " + String.join(", ", visitRecords);
    }
//    public void addAppointment(Appointment appointment) {
//        appointments.add(appointment);
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void addAppointment(Appointment appointment) {
    if (appointment == null) {
        System.out.println("Cannot add a null appointment.");
        return;
    }
    if (!appointments.contains(appointment)) {
        appointments.add(appointment);
        System.out.println("Appointment added for patient: " + getName());
        addVisitRecord("Appointment scheduled on " + appointment.getDate() + " with status: " + (appointment.getStatus() ));

    } else {
        System.out.println("Appointment is already added for patient: " + getName());
    }
}



    public List<Appointment> getAppointments() {
        return appointments;
    }




    public static Patient searchPatient(int patientID) {
        return patientTree.search(patientID);
    }

    public static String displayAllPatientsMain() {
        patientTree.displayPatients();
        return null;
    }
    public static String displayAllPatients() {
        return patientTree.getAllPatientsInfo();
    }
    public Appointment getAppointmentByID(int appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }

}

class PatientBST {
    private class Node {
        Patient patient;
        Node left, right;

        Node(Patient patient) {
            this.patient = patient;
            this.left = this.right = null;
        }
    }

    private Node root;

    public void insert(Patient patient) {
        root = insertRec(root, patient);
    }

    private Node insertRec(Node root, Patient patient) {
        if (root == null) {
            root = new Node(patient);
            return root;
        }
        if (patient.getPatientID() < root.patient.getPatientID()) {
            root.left = insertRec(root.left, patient);
        } else if (patient.getPatientID() > root.patient.getPatientID()) {
            root.right = insertRec(root.right, patient);
        }
        return root;
    }


    public Patient search(int patientID) {
        List<String> searchPath = new ArrayList<>();
        Patient result = searchRec(root, patientID, searchPath);


        System.out.println("Search Path: " + String.join(" -> ", searchPath));

        return result;
    }

    private Patient searchRec(Node root, int patientID, List<String> searchPath) {
        if (root == null) {
            return null;
        }


        searchPath.add(String.valueOf(root.patient.getPatientID()));
        if (root.patient.getPatientID() == patientID) {
            return root.patient;
        }

        if (patientID < root.patient.getPatientID()) {
            return searchRec(root.left, patientID, searchPath);
        }
        return searchRec(root.right, patientID, searchPath);
    }


    public void displayPatients() {
        displayPatientsRec(root);
    }

    private void displayPatientsRec(Node root) {
        if (root != null) {
            displayPatientsRec(root.left);
            System.out.println(root.patient.getPatientInfo());
            displayPatientsRec(root.right);
        }
    }
    public String getAllPatientsInfo() {
        StringBuilder sb = new StringBuilder();
        collectPatientsInfo(root, sb);
        return sb.toString();
    }

    private void collectPatientsInfo(Node root, StringBuilder sb) {
        if (root != null) {
            collectPatientsInfo(root.left, sb);
            sb.append(root.patient.getPatientInfo()).append("\n");
            collectPatientsInfo(root.right, sb);
        }
    }}