import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    Room(int roomNumber, String category, boolean isBooked) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = isBooked;
    }

    @Override
    public String toString() {
        return roomNumber + "," + category + "," + isBooked;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    String paymentStatus;

    Booking(String customerName, int roomNumber, String category, String paymentStatus) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return customerName + "," + roomNumber + "," + category + "," + paymentStatus;
    }
}

public class HotelReservationSystem {
    static final String ROOMS_FILE = "rooms.txt";
    static final String BOOKINGS_FILE = "bookings.txt";
    static ArrayList<Room> roomList = new ArrayList<>();
    static ArrayList<Booking> bookingList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        loadRooms();
        loadBookings();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Bookings");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1: viewAvailableRooms(); break;
                case 2: bookRoom(sc); break;
                case 3: cancelReservation(sc); break;
                case 4: viewBookings(); break;
                case 0: saveData(); System.out.println("Exiting..."); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }

    static void loadRooms() throws IOException {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                // Sample rooms
                pw.println("101,Standard,false");
                pw.println("102,Standard,false");
                pw.println("201,Deluxe,false");
                pw.println("202,Deluxe,false");
                pw.println("301,Suite,false");
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            roomList.add(new Room(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Boolean.parseBoolean(parts[2])
            ));
        }
        br.close();
    }

    static void loadBookings() throws IOException {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) file.createNewFile();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            bookingList.add(new Booking(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]));
        }
        br.close();
    }

    static void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room r : roomList) {
            if (!r.isBooked) {
                System.out.println("Room " + r.roomNumber + " (" + r.category + ")");
            }
        }
    }

    static void bookRoom(Scanner sc) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        viewAvailableRooms();
        System.out.print("Enter room number to book: ");
        int roomNo = sc.nextInt();
        sc.nextLine(); // consume newline

        for (Room r : roomList) {
            if (r.roomNumber == roomNo && !r.isBooked) {
                r.isBooked = true;

                // Simulate payment
                String payment = "Paid";

                Booking booking = new Booking(name, r.roomNumber, r.category, payment);
                bookingList.add(booking);
                System.out.println("Room " + roomNo + " booked successfully!");
                return;
            }
        }

        System.out.println("Room not available or does not exist.");
    }

    static void cancelReservation(Scanner sc) {
        System.out.print("Enter your name to cancel booking: ");
        String name = sc.nextLine();

        Iterator<Booking> iterator = bookingList.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.customerName.equalsIgnoreCase(name)) {
                iterator.remove();
                // free room
                for (Room r : roomList) {
                    if (r.roomNumber == b.roomNumber) {
                        r.isBooked = false;
                        break;
                    }
                }
                System.out.println("Booking cancelled for " + name);
                found = true;
                break;
            }
        }

        if (!found) System.out.println("No booking found under this name.");
    }

    static void viewBookings() {
        System.out.println("\n--- All Bookings ---");
        for (Booking b : bookingList) {
            System.out.println("Name: " + b.customerName + " | Room: " + b.roomNumber +
                               " (" + b.category + ") | Payment: " + b.paymentStatus);
        }
    }

    static void saveData() throws IOException {
        PrintWriter roomWriter = new PrintWriter(new FileWriter(ROOMS_FILE));
        for (Room r : roomList) {
            roomWriter.println(r);
        }
        roomWriter.close();

        PrintWriter bookingWriter = new PrintWriter(new FileWriter(BOOKINGS_FILE));
        for (Booking b : bookingList) {
            bookingWriter.println(b);
        }
        bookingWriter.close();
    }
}