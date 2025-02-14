
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Hframe extends JFrame {

    private final WaitingList waitingList = new WaitingList();
    PatientBST patientBST = new PatientBST();
    private final PatientManagementSystem system;

    public Hframe(PatientManagementSystem system) {
        this.system = system;
        createAndShowGUI();
    }

    public Hframe() {
        this.system = new PatientManagementSystem();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Patient Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        ImageIcon icon = new ImageIcon("D:\\AIU\\DS\\hospital2 (3)\\hospital2\\src\\download.jpeg");
        setIconImage(icon.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 1, 10, 10));

        JButton addPatientButton = new JButton("Add a New Patient");
        JButton searchEditPatientButton = new JButton("Search and Edit Patient by ID");
        JButton displayPatientsButton = new JButton("Display All Patients");
        JButton scheduleAppointmentButton = new JButton("Schedule an Appointment");
        JButton manageAppointmentsButton = new JButton("Manage Appointments for a Patient");
        JButton viewAppointmentsButton = new JButton("View Appointments for a Day");
        JButton viewWaitingListButton = new JButton("View Waiting List for a Day");
        JButton manageBillingButton = new JButton("Manage Billing");
        JButton generateReportsButton = new JButton("Generate Reports");
        JButton exitButton = new JButton("Exit");

        panel.add(addPatientButton);
        panel.add(searchEditPatientButton);
        panel.add(displayPatientsButton);
        panel.add(scheduleAppointmentButton);
        panel.add(manageAppointmentsButton);
        panel.add(viewAppointmentsButton);
        panel.add(viewWaitingListButton);
        panel.add(manageBillingButton);
        panel.add(generateReportsButton);
        panel.add(exitButton);

        add(panel);

        addPatientButton.addActionListener(e -> addNewPatient());
        searchEditPatientButton.addActionListener(e -> searchAndEditPatientByID());
        displayPatientsButton.addActionListener(e -> displayAllPatients());
        scheduleAppointmentButton.addActionListener(e -> scheduleAppointment());
        manageAppointmentsButton.addActionListener(e -> manageAppointments());
        viewAppointmentsButton.addActionListener(e -> viewAppointmentsForDay());
        viewWaitingListButton.addActionListener(e -> viewWaitingListForDay());
        manageBillingButton.addActionListener(e -> manageBilling());
        generateReportsButton.addActionListener(e -> generateReports());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void addNewPatient() {
        try {
            String name = JOptionPane.showInputDialog("Enter patient name:");
            String ageString = JOptionPane.showInputDialog("Enter patient age:");
            int age = Integer.parseInt(ageString);
            String contactInfo = JOptionPane.showInputDialog("Enter contact info:");
            String medicalHistory = JOptionPane.showInputDialog("Enter medical history:");


            Patient newPatient = new Patient(name, age, contactInfo, medicalHistory);

            JOptionPane.showMessageDialog(null, "New patient added successfully!\nPatient ID: " + newPatient.getPatientID());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid data.");
        }
    }

    private void searchAndEditPatientByID() {
        try {
            String idString = JOptionPane.showInputDialog("Enter patient ID to search:");
            int id = Integer.parseInt(idString);
            Patient patient = Patient.searchPatient(id);
            if (patient == null) {
                JOptionPane.showMessageDialog(null, "Patient not found.");
                return;
            }

            String newName = JOptionPane.showInputDialog("Enter new name (leave blank to keep current):", patient.getName());
            if (!newName.isBlank()) patient.setName(newName);

            String newAgeString = JOptionPane.showInputDialog("Enter new age (or -1 to keep current):", String.valueOf(patient.getAge()));
            int newAge = Integer.parseInt(newAgeString);
            if (newAge != -1) patient.setAge(newAge);

            String newContactInfo = JOptionPane.showInputDialog("Enter new contact info (leave blank to keep current):", patient.getPatientInfo());
            if (!newContactInfo.isBlank()) patient.setContactInfo(newContactInfo);

            String newMedicalHistory = JOptionPane.showInputDialog("Enter new medical history (leave blank to keep current):", patient.getPatientInfo());
            if (!newMedicalHistory.isBlank()) patient.setMedicalHistory(newMedicalHistory);

            JOptionPane.showMessageDialog(null, "Patient information updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }

    private static void displayAllPatients() {
        SwingUtilities.invokeLater(() -> {
            JTextArea textArea = new JTextArea(20, 40);
            textArea.setText(Patient.displayAllPatients());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Patient List", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void scheduleAppointment() {
        try {
            String idString = JOptionPane.showInputDialog("Enter patient ID to schedule appointment:");
            int id = Integer.parseInt(idString);
            String date = JOptionPane.showInputDialog("Enter appointment date (YYYY-MM-DD):");
            String time = JOptionPane.showInputDialog("Enter appointment time (e.g., 10:30 AM):");

            Patient patient = Patient.searchPatient(id);
            if (patient == null) {
                JOptionPane.showMessageDialog(null, "Patient not found.");
                return;
            }

            ArrayList<Appointment> dailyAppointments = Appointment.getAppointmentsForDate(date);
            if (dailyAppointments.size() >= 2) {
                WaitingList.addToWaitList(patient, date);
                JOptionPane.showMessageDialog(null, "Maximum appointments reached for the day. Added to waiting list.");
            } else {
                new Appointment(patient, date, time);
                JOptionPane.showMessageDialog(null, "Appointment scheduled successfully.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }

    private void manageAppointments() {
        try {
            String idString = JOptionPane.showInputDialog("Enter patient ID to manage appointments:");
            int id = Integer.parseInt(idString);
            Patient patient = Patient.searchPatient(id);
            if (patient == null) {
                JOptionPane.showMessageDialog(null, "Patient not found.");
                return;
            }

            String[] options = {"Cancel Appointment", "Reschedule Appointment"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Manage Appointments",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                String appIDString = JOptionPane.showInputDialog("Enter appointment ID to cancel:");
                int appID = Integer.parseInt(appIDString);
                Appointment app = patient.getAppointmentByID(appID);
                if (app != null) {
                    double appointmentCost = app.getPrice();
                    app.cancel();
                    JOptionPane.showMessageDialog(null, "Appointment canceled successfully.");
                    patient.updateVisitRecord(app.getDate(), "Cancelled");
                    Billing billing = patient.getBilling();
                    if (billing != null && appointmentCost > 0) {
                        billing.cancelBill(appointmentCost);
                    } else {
                        JOptionPane.showMessageDialog(null, "No valid billing record found. Billing update skipped.");
                    }
                    ArrayList<Appointment> dailyAppointments = Appointment.getAppointmentsForDate(app.getDate());
                    WaitingList.assignNextAppointment(app.getDate(), app.getTime(), dailyAppointments);
                } else {
                    JOptionPane.showMessageDialog(null, "Appointment not found.");
                }
            } else if (choice == 1) {
                String appIDString = JOptionPane.showInputDialog("Enter appointment ID to reschedule:");
                int appID = Integer.parseInt(appIDString);
                String newDate = JOptionPane.showInputDialog("Enter new date:");
                String newTime = JOptionPane.showInputDialog("Enter new time:");
                Appointment app = patient.getAppointmentByID(appID);
                if (app != null) {
                    patient.updateVisitRecord(app.getDate(), "Rescheduled");
                    patient.addVisitRecord("Date: " + newDate + ", Time: " + newTime + ", Status: Scheduled");
                    app.rescheduleAppointment(newDate, newTime);
                    JOptionPane.showMessageDialog(null, "Appointment rescheduled successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Appointment not found.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }
    private void viewAppointmentsForDay() {
        String date = JOptionPane.showInputDialog("Enter date to view appointments (YYYY-MM-DD):");
        ArrayList<Appointment> appointments = Appointment.getAppointmentsForDate(date);
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No appointments found for this day.");
        } else {
            StringBuilder result = new StringBuilder("Appointments:\n");
            for (Appointment app : appointments) {
                result.append(app.getAppointmentDetails()).append("\n");
            }
            JOptionPane.showMessageDialog(null, result.toString());
        }
    }

    private void viewWaitingListForDay() {
        String date = JOptionPane.showInputDialog("Enter date to view waiting list (YYYY-MM-DD):");
        ArrayList<Patient> waitingListForDate = waitingList.getWaitingListForDate(date);
        if (waitingListForDate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No patients in the waiting list for this day.");
        } else {
            StringBuilder result = new StringBuilder("Waiting List:\n");
            for (Patient patient : waitingListForDate) {
                result.append("ID: ").append(patient.getPatientID())
                        .append(", Name: ").append(patient.getName())
                        .append(", Age: ").append(patient.getAge()).append("\n");
            }
            JOptionPane.showMessageDialog(null, result.toString());
        }
    }

    private void manageBilling() {
        try {
            String idString = JOptionPane.showInputDialog("Enter patient ID to manage billing:");
            int id = Integer.parseInt(idString);
            Patient patient = Patient.searchPatient(id);
            if (patient == null) {
                JOptionPane.showMessageDialog(null, "Patient not found.");
                return;
            }

            Billing billing = patient.getBilling();
            String[] options = {"View Billing Details", "Add Payment", "Check Payment Status"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Manage Billing",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                JTextArea textArea = new JTextArea(20, 40);
                textArea.setText(billing.printBillingDetailsGUI());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Billing Details", JOptionPane.INFORMATION_MESSAGE);
            } else if (choice == 1) {
                String paymentString = JOptionPane.showInputDialog("Enter payment amount:");
                double payment = Double.parseDouble(paymentString);
                billing.addPayment(payment);
                JOptionPane.showMessageDialog(null, "Payment added successfully.");
            } else if (choice == 2) {
                JTextArea textArea = new JTextArea(20, 40);
                textArea.setText(billing.getPaymentStatusGUI());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Payment Status", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }
    private void generateReports() {
        String[] options = {"Patient Report", "Appointment Report", "Revenue Report"};
        int choice = JOptionPane.showOptionDialog(null, "Choose a report to generate:", "Generate Reports",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> {
                String idString = JOptionPane.showInputDialog("Enter patient ID for the report:");
                int id = Integer.parseInt(idString);
                Patient patient = Patient.searchPatient(id);
                if (patient == null) {
                    JOptionPane.showMessageDialog(null, "Patient not found.");
                } else {
                    JTextArea textArea = new JTextArea(20, 40);
                    textArea.setText(new ReportGenerator().generatePatientReportGUI(patient));
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Patient Report", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            case 1 -> {
                String idString = JOptionPane.showInputDialog("Enter patient ID for appointment report:");
                int id = Integer.parseInt(idString);
                Patient patient = Patient.searchPatient(id);
                if (patient == null) {
                    JOptionPane.showMessageDialog(null, "Patient not found.");
                } else {
                    JTextArea textArea = new JTextArea(20, 40);
                    textArea.setText(new ReportGenerator().generateAppointmentReportGUI(patient));
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Appointment Report", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            case 2 -> {
                ArrayList<Billing> allBillings = Billing.getAllBillings();
                JTextArea textArea = new JTextArea(20, 40);
                textArea.setText(new ReportGenerator().generateRevenueReportGUI(allBillings));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Revenue Report", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}