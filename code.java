import java.util.*;

class Node {
    long phoneNo;
    String name;
    String email;
    String address;
    Node link;

    Node(long data, String name, String email, String address) {
        this.phoneNo = data;
        this.name = name;
        this.email = email;
        this.address = address;
        this.link = null;
    }
}

class HashD extends Thread {
    static long phone;
    static String name;
    static String email;
    static String address;
    static Node[] hashT = new Node[100];
    static Node[] stack = new Node[100];
    static int top = -1;

    public void run() {
        Node newnode = new Node(phone, name, email, address);
        long ind = phone % 100;
        int index = (int) ind;
        if (hashT[index] == null) {
            hashT[index] = newnode;
        } else {
            Node ptr;
            ptr = hashT[index];
            while (ptr.link != null) {
                ptr = ptr.link;
            }
            ptr.link = newnode;
        }
        top++;
        stack[top] = newnode;
    }

    public static void undoLastContact() {
        if (top >= 0) {
            Node lastContact = stack[top];
            long ind = lastContact.phoneNo % 100;
            int index = (int) ind;
            if (hashT[index] == lastContact) {
                hashT[index] = null;
                stack[top]=null;
            } else {
                Node ptr = hashT[index];
                while (ptr.link != lastContact) {
                    ptr = ptr.link;
                }
                ptr.link = null;
                stack[top]=null;
            }top--;
            System.out.println("Last contact undone successfully.");
        } else {System.out.println("No contacts to undo.");
        }
    }

    public static void modifyLastContact() {
        if (top >= 0) {
            Node lastContact = stack[top];
            Scanner scanner = new Scanner(System.in);
            System.out.println("Modify Contact:");
            System.out.print("Enter Name : ");
            lastContact.name = scanner.nextLine();
            System.out.print("Enter Email : ");
            lastContact.email = scanner.nextLine();
            System.out.print("Enter Address : ");
            lastContact.address = scanner.nextLine();
            System.out.print("Enter phone : ");
            lastContact.phoneNo = scanner.nextLong();
            System.out.println("Contact modified successfully.");
        } else {
            System.out.println("No contacts to modify.");
        }
    }
}

public class Main {
    static LinkedList<String> callLog = new LinkedList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) { // Infinite loop until user exits
            System.out.println("Menu");
            System.out.println("1. Add new contact");
            System.out.println("2. Modify existing contact");
            System.out.println("3. Display all contacts");
            System.out.println("4. Make a call");
            System.out.println("5. Display call log");
            System.out.println("6. Exit");
            System.out.print("Enter choice : ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addContact(sc);
                    break;
                case 2:
                    modifyContact();
                    break;
                case 3:
                    displayAllContacts();
                    break;
                case 4:
                    makeCall(sc);
                    break;
                case 5:
                    displayCallLog();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void addContact(Scanner sc) {
        System.out.print("Enter Name : ");
        String name = sc.nextLine();
        System.out.print("Enter Email : ");
        String email = sc.nextLine();
        System.out.print("Enter Address : ");
        String address = sc.nextLine();
        System.out.print("Enter phone : ");
        long phone = sc.nextLong();
        saveDetails(name, email, phone, address);
    }

    public static void saveDetails(String name, String email, long phoneNo, String address) {
        synchronized (HashD.class) {
            HashD.phone = phoneNo;
            HashD.name = name;
            HashD.email = email;
            HashD.address = address;
            HashD hashing = new HashD();
            hashing.start();
        }
    }

    public static void modifyContact() {
        synchronized (HashD.class) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Modify Contact:");
            System.out.println("1. Undo last contact");
            System.out.println("2. Modify last contact");
            System.out.print("Enter choice : ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    HashD.undoLastContact();
                    break;
                case 2:
                    HashD.modifyLastContact();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void displayAllContacts() {
        System.out.println("Displaying all contacts:");
        for (Node contact : HashD.stack) {
            if (contact != null) {
                System.out.println("Name: " + contact.name);
                System.out.println("Phone: " + contact.phoneNo);
                System.out.println("Email: " + contact.email);
                System.out.println("Address: " + contact.address);
                System.out.println("-------------------------");
            }
        }
    }

    public static void makeCall(Scanner sc) {
        System.out.print("Enter the number you want to call: ");
        String number = sc.next();
        callLog.add("Called: " + number + " at " + new Date());
        System.out.println("Call made successfully to " + number);
    }

    public static void displayCallLog() {
        System.out.println("Displaying call log:");
        for (String call : callLog) {
            System.out.println(call);
        }
    }
}
