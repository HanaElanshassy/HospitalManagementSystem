import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    private String ReportType;
    private String data;
Billing billing;
    public  void generatePatientReport(Patient p){
        ReportType = "Patient report";
        data="";
        data+=p.toString();
        data+="\n";
        System.out.println("This report is "+ReportType);
        System.out.println(data);
    }
    public void generateAppointmentReport(Patient p){
        ReportType = "Appointment report";
        data="";
        mergeSort(p.appointments);

        for (int i=0;i<p.appointments.size();i++){
            data+=p.appointments.get(i).toString();
            data+="\n";
        }
        System.out.println("This report is "+ReportType);
        System.out.println(data);
    }
    public void generateAppointmentReport(Appointment a){
        ReportType = "Appointment report";
        data="";
        data+=a.patient.toString();
        data+="\n";
        data+=a.toString();
        System.out.println("This report is "+ReportType);
        System.out.println(data);

    }
    public void generateRevenueReport(ArrayList<Billing>bills){
        ReportType = "Revenue report";
        double sum = 0 ;
        for (int i = 0  ; i<bills.size() ; i++)
            for (int j = 0 ; j<bills.get(i).paymentHistory.size(); j++){
                sum+=bills.get(i).paymentHistory.get(j);

            }
        System.out.println("This report is "+ReportType);
        System.out.println("Total revenue = "+sum);



    }
    public String generatePatientReportGUI(Patient p) {
        ReportType = "Patient report";
        data = "";
        data += p.toString();
        data += "\n";
        return "This report is " + ReportType + "\n" + data;
    }

    public String generateAppointmentReportGUI(Patient p) {
        ReportType = "Appointment report";
        data = "";
        mergeSort(p.appointments);

        for (int i = 0; i < p.appointments.size(); i++) {
            data += p.appointments.get(i).toString();
            data += "\n";
        }
        return "This report is " + ReportType + "\n" + data;
    }

    public String generateRevenueReportGUI(ArrayList<Billing> bills) {
        ReportType = "Revenue report";
        double sum = 0;
        for (int i = 0; i < bills.size(); i++) {
            for (int j = 0; j < bills.get(i).paymentHistory.size(); j++) {
                sum += bills.get(i).paymentHistory.get(j);
            }
        }
        return "This report is " + ReportType + "\nTotal revenue = " + sum;
    }

    public static void mergeSort(List<Appointment> appointments) {
        if (appointments.size() > 1) {
            int mid = appointments.size() / 2;
            List<Appointment> left = new ArrayList<>(appointments.subList(0, mid));
            List<Appointment> right = new ArrayList<>(appointments.subList(mid, appointments.size()));

            mergeSort(left);
            mergeSort(right);


            merge(appointments, left, right);
        }
    }


    private static void merge(List<Appointment> appointments, List<Appointment> left, List<Appointment> right) {
        int leftIndex = 0, rightIndex = 0, mergeIndex = 0;


        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getDate().compareTo(right.get(rightIndex).getDate()) <= 0) {
                appointments.set(mergeIndex++, left.get(leftIndex++));
            } else {
                appointments.set(mergeIndex++, right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            appointments.set(mergeIndex++, left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            appointments.set(mergeIndex++, right.get(rightIndex++));
        }
    }


}
