//Hana Sherif Abdelmoniem Mohamed Elanshassy 23101499
//Jana mohamed ali 23101867
//Nour ahmed rashad 23101495
//Mayar Ahmed Elsayed 23102154

import java.util.*;

public class PatientManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static WaitingList waitingList = new WaitingList();
    private static ReportGenerator reportGenerator = new ReportGenerator();

    public static void main(String[] args) {
        System.out.println("Welcome to the Patient Management System!");

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addNewPatient();
                    break;
                case 2:
                    searchAndEditPatientByID();
                    break;
                case 3:
                    displayAllPatients();
                    break;
                case 4:
                    scheduleAppointment();
                    break;
                case 5:
                    manageAppointments();
                    break;
                case 6:
                    viewAppointmentsForDay();
                    break;
                case 7:
                    viewWaitingListForDay();
                    break;
                case 8:
                    manageBilling();
                    break;
                case 9:
                    generateReports();
                    break;
                case 10:
                    exitSystem();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Add a new patient");
        System.out.println("2. Search and edit a patient by ID");
        System.out.println("3. Display all patients");
        System.out.println("4. Schedule an appointment");
        System.out.println("5. Manage appointments for a patient");
        System.out.println("6. View appointments for a specific day");
        System.out.println("7. View waiting list for a specific day");
        System.out.println("8. Manage Billing");
        System.out.println("9. Generate reports");
        System.out.println("10. Exit");
    }

    private static void addNewPatient() {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter patient contact info: ");
        String contactInfo = scanner.nextLine();
        System.out.print("Enter patient medical history: ");
        String medicalHistory = scanner.nextLine();

        Patient newPatient = new Patient(name, age, contactInfo, medicalHistory);
        System.out.println("New patient added successfully!");
        System.out.println("Patient ID: " + newPatient.getPatientID());
    }

    private static void searchAndEditPatientByID() {
        System.out.print("Enter patient ID to search: ");
        int searchID = scanner.nextInt();
        scanner.nextLine();

        Patient foundPatient = Patient.searchPatient(searchID);
        if (foundPatient != null) {
            System.out.println("Patient found:");
            System.out.println(foundPatient.getPatientInfo());

            System.out.println("Do you want to edit this patient’s information? (yes/no)");
            String editChoice = scanner.nextLine();

            if (editChoice.equalsIgnoreCase("yes")) {
                System.out.print("Enter new name (leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (!newName.isBlank()) {
                    foundPatient.setName(newName);
                }

                System.out.print("Enter new age (or -1 to keep current): ");
                int newAge = scanner.nextInt();
                scanner.nextLine();
                if (newAge != -1) {
                    foundPatient.setAge(newAge);
                }

                System.out.print("Enter new contact info (leave blank to keep current): ");
                String newContactInfo = scanner.nextLine();
                if (!newContactInfo.isBlank()) {
                    foundPatient.setContactInfo(newContactInfo);
                }

                System.out.print("Enter new medical history (leave blank to keep current): ");
                String newMedicalHistory = scanner.nextLine();
                if (!newMedicalHistory.isBlank()) {
                    foundPatient.setMedicalHistory(newMedicalHistory);
                }

                System.out.println("Patient information updated successfully!");
            }
        } else {
            System.out.println("No patient found with ID: " + searchID);
        }
    }


    private static void displayAllPatients() {
        System.out.println("All patients:");
        Patient.displayAllPatientsMain();
    }

    private static void scheduleAppointment() {
        System.out.print("Enter patient ID to schedule an appointment for: ");
        int scheduleID = scanner.nextInt();
        scanner.nextLine();
        Patient patientForAppointment = Patient.searchPatient(scheduleID);

        if (patientForAppointment != null) {
            System.out.print("Enter appointment date (e.g., 2024-12-25): ");
            String date = scanner.nextLine();
            System.out.print("Enter appointment time (e.g., 10:30 AM): ");
            String time = scanner.nextLine();

            ArrayList<Appointment> dailyAppointments = Appointment.getAppointmentsForDate(date);
            if (dailyAppointments.size() >= 2) {
                System.out.println("Maximum appointments reached for this day. Adding patient to the waiting list.");
                waitingList.addToWaitList(patientForAppointment, date);
            } else {
                Appointment appointment = new Appointment(patientForAppointment, date, time);
                appointment.schedule();
                dailyAppointments.add(appointment);
                System.out.println("Appointment scheduled successfully for Patient ID " + scheduleID);
            }
        } else {
            System.out.println("No patient found with ID: " + scheduleID);
        }
    }

    private static void manageAppointments() {
        System.out.print("Enter patient ID to manage appointments: ");
        int manageID = scanner.nextInt();
        scanner.nextLine();
        Patient patientToManage = Patient.searchPatient(manageID);

        if (patientToManage != null) {
            System.out.println("Appointments for patient ID " + manageID + ":");
            patientToManage.displayAppointments();

            System.out.println("Choose an option:");
            System.out.println("1. Cancel an appointment");
            System.out.println("2. Reschedule an appointment");
            System.out.println("3. Return to main menu");

            int manageChoice = scanner.nextInt();
            scanner.nextLine();

            switch (manageChoice) {
                case 1:
                    cancelAppointment(patientToManage);
                    break;
                case 2:
                    rescheduleAppointment(patientToManage);
                    break;
                case 3:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Returning to main menu.");
            }
        } else {
            System.out.println("No patient found with ID: " + manageID);
        }
    }

    private static void cancelAppointment(Patient patient) {
        System.out.print("Enter appointment ID to cancel: ");
        int cancelID = scanner.nextInt();
        scanner.nextLine();

        Appointment cancelAppointment = patient.getAppointmentByID(cancelID);

        if (cancelAppointment != null) {
            double appointmentCost = cancelAppointment.getPrice();

            cancelAppointment.cancel();
            System.out.println("Appointment canceled.");
            patient.updateVisitRecord(cancelAppointment.getDate(), "Cancelled");

            Billing billing = patient.getBilling();
            if (billing != null && appointmentCost > 0) {
                billing.cancelBill(appointmentCost);
            } else {
                System.out.println("No valid billing record found. Billing update skipped.");
            }


            ArrayList<Appointment> dailyAppointments = Appointment.getAppointmentsForDate(cancelAppointment.getDate());
            waitingList.assignNextAppointment(cancelAppointment.getDate(), cancelAppointment.getTime(), dailyAppointments);
        } else {
            System.out.println("Failed to cancel appointment. Check the appointment ID.");
        }
    }


    private static void rescheduleAppointment(Patient patient) {
        System.out.print("Enter appointment ID to reschedule: ");
        int rescheduleID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new date: ");
        String newDate = scanner.nextLine();
        System.out.print("Enter new time: ");
        String newTime = scanner.nextLine();
        Appointment rescheduleAppointment = patient.getAppointmentByID(rescheduleID);
        if (rescheduleAppointment != null) {
            patient.updateVisitRecord(rescheduleAppointment.getDate(), "Rescheduled");
            patient.addVisitRecord("Date: " + newDate + ", Time: " + newTime + ", Status: Scheduled");
            rescheduleAppointment.rescheduleAppointment(newDate, newTime);
            String newVisitRecord = "Date: " + newDate + ", Time: " + newTime + ", Status: Scheduled";
            patient.addVisitRecord(newVisitRecord);
        } else {
            System.out.println("Failed to reschedule appointment. Check the appointment ID.");
        }
    }

    private static void viewAppointmentsForDay() {
        System.out.print("Enter the date to view appointments (e.g., 2024-12-25): ");
        String viewDate = scanner.nextLine();

        ArrayList<Appointment> appointmentsForDay = Appointment.getAppointmentsForDate(viewDate);
        if (appointmentsForDay.isEmpty()) {
            System.out.println("No appointments scheduled for " + viewDate);
        } else {
            System.out.println("Appointments for " + viewDate + ":");
            for (Appointment appointment : appointmentsForDay) {
                System.out.println(appointment.getPatient().getName() + " - " + appointment.getTime() + " - " + appointment.getStatus());
            }
        }
    }

    private static void viewWaitingListForDay() {
        System.out.print("Enter the date to view the waiting list (e.g., 2024-12-25): ");
        String waitingListDate = scanner.nextLine();

        waitingList.updateWaitingList(waitingListDate);
        ArrayList<Patient> waitingPatientsForDay = waitingList.getWaitingListForDate(waitingListDate);

        if (waitingPatientsForDay.isEmpty()) {
            System.out.println("No patients on the waiting list for " + waitingListDate);
        } else {
            System.out.println("Waiting list for " + waitingListDate + ":");
            for (Patient patient : waitingPatientsForDay) {
                System.out.println("Patient ID: " + patient.getPatientID() + ", Name: " + patient.getName() + ", Age: " + patient.getAge());
            }
        }
    }

    private static void manageBilling() {
        System.out.print("Enter patient ID to manage billing: ");
        int billingID = scanner.nextInt();
        scanner.nextLine();
        Patient patientForBilling = Patient.searchPatient(billingID);

        if (patientForBilling != null) {
            Billing billing = patientForBilling.getBilling();

            System.out.println("Billing Options:");
            System.out.println("1. View Billing Details");
            System.out.println("2. Add Payment");
            System.out.println("3. Check Payment Status");
            System.out.println("4. Return to PatientManagementSystem Menu");

            int billingChoice = scanner.nextInt();
            scanner.nextLine();

            switch (billingChoice) {
                case 1:
                    billing.printBillingDetails();
                    break;
                case 2:
                    System.out.print("Enter payment amount: ");
                    double paymentAmount = scanner.nextDouble();
                    scanner.nextLine();
                    billing.addPayment(paymentAmount);
                    break;
                case 3:
                    billing.getPaymentStatus();
                    break;
                case 4:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Returning to main menu.");
            }
        } else {
            System.out.println("No patient found with ID: " + billingID);
        }
    }

    private static void generateReports() {
        System.out.println("Choose a report to generate:");
        System.out.println("1. Patient Report");
        System.out.println("2. Appointment Report");
        System.out.println("3. Revenue Report");
        int reportChoice = scanner.nextInt();
        scanner.nextLine();

        switch (reportChoice) {
            case 1:
                generatePatientReport();
                break;
            case 2:
                generateAppointmentReport();
                break;
            case 3:
                generateRevenueReport();
                break;
            default:
                System.out.println("Invalid report choice.");
        }
    }

    private static void generatePatientReport() {
        System.out.print("Enter patient ID for the report: ");
        int patientID = scanner.nextInt();
        scanner.nextLine();
        Patient patientForReport = Patient.searchPatient(patientID);

        if (patientForReport != null) {
            reportGenerator.generatePatientReport(patientForReport);
        } else {
            System.out.println("No patient found with ID: " + patientID);
        }
    }

    private static void generateAppointmentReport() {
        System.out.print("Enter patient ID for appointment report: ");
        int patientID = scanner.nextInt();
        scanner.nextLine();
        Patient patientForAppointments = Patient.searchPatient(patientID);

        if (patientForAppointments != null) {
            reportGenerator.generateAppointmentReport(patientForAppointments);
        } else {
            System.out.println("No patient found with ID: " + patientID);
        }
    }

    private static void generateRevenueReport() {
        System.out.println("Generating revenue report...");
        ArrayList<Billing> allBillings = Billing.getAllBillings();
        reportGenerator.generateRevenueReport(allBillings);
    }

    private static void exitSystem() {
        System.out.println("Exiting the system. Goodbye!");
        scanner.close();
    }
}
