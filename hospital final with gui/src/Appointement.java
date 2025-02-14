import java.util.ArrayList;
import java.util.List;

class Appointment {
    private static int appointmentCounter = 1;
    private static List<Appointment> allAppointments = new ArrayList<>();
    private int appointmentID;
    public Patient patient;
    private String date;
    private String time;
    private String status;
    private final double price = 250.0;
    public WaitingList waitingList;
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID=" + appointmentID +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public Appointment(String date, String time, String status) {
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDate() {
        return date;
    }


    public Appointment(Patient patient, String date, String time) {
        this.appointmentID = generateUniqueID();
        this.patient = patient;
        this.date = date;
        this.time = time;
        this.status = "Scheduled";
        patient.addAppointment(this);
        patient.getBilling().generateBill(this.price);
        allAppointments.add(this);
    }
    public double getPrice() {
        return price;
    }

    public Appointment() {
    }


    private int generateUniqueID() {
        return appointmentCounter++;
    }


    public void schedule() {
        this.status = "Scheduled";
        System.out.println("Appointment scheduled successfully:");
        System.out.println(getAppointmentDetails());
    }


    public void cancel() {
        this.status = "Canceled";
        allAppointments.remove(this);
        System.out.println("Appointment has been canceled:");
        System.out.println(getAppointmentDetails());
    }




    public void rescheduleAppointment(String newDate, String newTime) {
        this.date = newDate;
        this.time = newTime;
        this.status = "Rescheduled";
        System.out.println("Appointment has been rescheduled:");
        System.out.println(getAppointmentDetails());
    }


    public String getAppointmentDetails() {
        return "Appointment ID: " + appointmentID +
                "\nPatient ID: " + patient.getPatientID() +
                "\nPatient Name: " + patient.getName() +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nStatus: " + status;
    }


    public int getAppointmentID() {
        return appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getStatus() {
        return status;
    }


    public static ArrayList<Appointment> getAppointmentsForDate(String date) {
        ArrayList<Appointment> appointmentsForDate = new ArrayList<>();
        for (Appointment appointment : allAppointments) {
            if (appointment.getDate().equals(date) && appointment.getStatus().equals("Scheduled")) {
                appointmentsForDate.add(appointment);
            }
        }
        return appointmentsForDate;
    }

}
