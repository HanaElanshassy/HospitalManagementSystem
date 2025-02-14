import java.util.ArrayList;

class Billing {
    protected Patient patient;
    protected static final ArrayList<Billing> allBillings = new ArrayList<>();

    protected final int patientId;
    protected double total;
    protected final ArrayList<Double> paymentHistory;

    public Billing(int patientId, Patient patient) {
        this.patientId = patientId;
        this.patient = patient;
        this.total = 0;
        this.paymentHistory = new ArrayList<>();
        allBillings.add(this);
    }

    public String getPaymentStatusGUI() {
        double sum = 0;
        for (double payment : paymentHistory) {
            sum += payment;
        }
        if (sum == total) {
            return "Fully paid";
        } else {
            return String.format("Partially paid. Remaining balance: $%.2f", (total - sum));
        }
    }


    public String printBillingDetailsGUI() {
        StringBuilder details = new StringBuilder();
        details.append("Patient ID: ").append(patientId).append("\n");
        details.append("Total Amount Due: $").append(total).append("\n");
        details.append("Payment History: ").append(paymentHistory).append("\n");
        return details.toString();
    }
    public static ArrayList<Billing> getAllBillings() {
        return new ArrayList<>(allBillings);
    }


    public int getPatientId() {
        return patientId;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<Double> getPaymentHistory() {
        return new ArrayList<>(paymentHistory);
    }

    public void generateBill(double price) {
        if (price <= 0) {
            System.out.println("Invalid price. Price must be positive.");
            return;
        }
        total += price;
    }

    public void cancelBill(double price) {
        if (price <= 0) {
            System.out.println("Invalid price. Price must be positive.");
            return;
        }
        if (total < price) {
            System.out.println("Error: Cancellation amount exceeds total due.");
            return;
        }
        total -= price;
        System.out.printf("Bill updated. $%.2f has been deducted from the total.%n", price);
    }


    public void addPayment(double payment) {
        if (payment <= 0) {
            System.out.println("Invalid payment amount. Payment must be positive.");
            return;
        }
        paymentHistory.add(payment);
    }

         public void getPaymentStatus() {
        double sum = 0;
        for (double payment : paymentHistory) {
            sum += payment;
        }
        if (sum == total) {
            System.out.println("Fully paid");
        } else {
            System.out.printf("Partially paid. Remaining balance: $%.2f%n", (total - sum));
        }
    }

    public Object printBillingDetails() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Total Amount Due: $" + total);
        System.out.println("Payment History: " + paymentHistory);
        return null;
    }
}
