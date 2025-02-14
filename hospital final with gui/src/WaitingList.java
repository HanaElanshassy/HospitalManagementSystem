import java.util.*;
public class WaitingList {

    private static class HeapNode {
        Patient patient;
        String date;
        public HeapNode(Patient patient, String date) {
            this.patient = patient;
            this.date = date;
        }
    }

    private static ArrayList<HeapNode> heap = new ArrayList<>();
    private static Map<String, ArrayList<Patient>> dateToWaitingList = new HashMap<>();

    public WaitingList() {
        heap = new ArrayList<>();
        dateToWaitingList = new HashMap<>();
    }

    public static void addToWaitList(Patient patient, String date) {
        HeapNode newNode = new HeapNode(patient, date);
        heap.add(newNode);
        dateToWaitingList.putIfAbsent(date, new ArrayList<>());
        dateToWaitingList.get(date).add(patient);
        heapifyUp(heap.size() - 1);
        System.out.println("Patient added to the waiting list: " + patient.getName() + " for date: " + date);
    }

    public static Patient removeFromWaitList() {
        if (heap.isEmpty()) {
            System.out.println("Waiting list is empty.");
            return null;
        }
        HeapNode removedNode = heap.get(0);
        Patient patient = removedNode.patient;
        String date = removedNode.date;


        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapifyDown(0);


        ArrayList<Patient> waitingListForDate = dateToWaitingList.get(date);
        if (waitingListForDate != null) {
            waitingListForDate.remove(patient);
            if (waitingListForDate.isEmpty()) {
                dateToWaitingList.remove(date);
            }
        }

        System.out.println("Patient removed from the waiting list: " + patient.getName());
        return patient;
    }

    public static void displayWaitingList() {
        if (heap.isEmpty()) {
            System.out.println("Waiting list is empty.");
            return;
        }
        System.out.println("Current Waiting List:");
        for (HeapNode node : heap) {
            System.out.println("Patient ID: " + node.patient.getPatientID() + ", Name: " + node.patient.getName() + ", Age: " + node.patient.getAge());
        }
    }

    public static void assignNextAppointment(String date, String time, ArrayList<Appointment> dailyAppointments) {
        if (dailyAppointments.size() >= 2) {
            System.out.println("Maximum appointments reached for the day.");
            return;
        }
        Patient nextPatient = removeFromWaitList();
        if (nextPatient == null) {
            System.out.println("No patients in the waiting list to assign an appointment.");
            return;
        }
        Appointment newAppointment = new Appointment(nextPatient, date, time);
        newAppointment.schedule();
        dailyAppointments.add(newAppointment);
    }

    private static void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).patient.getAge() > heap.get(parentIndex).patient.getAge()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private static void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int largest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (leftChild < size && heap.get(leftChild).patient.getAge() > heap.get(largest).patient.getAge()) {
                largest = leftChild;
            }
            if (rightChild < size && heap.get(rightChild).patient.getAge() > heap.get(largest).patient.getAge()) {
                largest = rightChild;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    private static void swap(int i, int j) {
        HeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public ArrayList<Patient> getWaitingListForDate(String date) {
        return dateToWaitingList.getOrDefault(date, new ArrayList<>());
    }

    public void updateWaitingList(String date) {
        ArrayList<Patient> waitingPatients = getWaitingListForDate(date);
        ArrayList<Appointment> dailyAppointments = Appointment.getAppointmentsForDate(date);

        int availableSlots = 2 - dailyAppointments.size();
        String[] timeSlots = {"10:00 AM", "02:00 PM"};

        for (String slot : timeSlots) {
            if (availableSlots <= 0 || waitingPatients.isEmpty()) {
                break;
            }
            boolean isTaken = dailyAppointments.stream().anyMatch(a -> a.getTime().equals(slot));
            if (!isTaken) {
                Patient nextPatient = removeFromWaitList();
                if (nextPatient == null) break;

                Appointment newAppointment = new Appointment(nextPatient, date, slot);
                newAppointment.schedule();
                dailyAppointments.add(newAppointment);
                availableSlots--;
            }
        }
    }

}
