
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class OnlineReservationSystem {

    private static boolean[][] seats = new boolean[5][10]; 
    private static JButton[][] seatButtons = new JButton[5][10]; 
    private static JButton reserveButton; 
    private static int selectedRow = -1;
    private static int selectedSeat = -1;

    private static Map<Integer, String> seatNames = new HashMap<>(); 

    public static void main(String[] args) {
        JFrame frame = new JFrame("Online Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1)); 

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 10)); 

        for (int row = 0; row < 5; row++) {
            for (int seat = 0; seat < 10; seat++) {
                seatButtons[row][seat] = new JButton((char) ('A' + row) + Integer.toString(seat + 1));
                seatButtons[row][seat].setBackground(Color.CYAN);
                seatPanel.add(seatButtons[row][seat]);
                final int rowNumber = row;
                final int seatNumber = seat;
                seatButtons[row][seat].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectSeat(rowNumber, seatNumber);
                    }
                });
            }
        }

        JPanel buttonPanel = new JPanel();
        reserveButton = new JButton("Reserve Seat");
        JButton viewButton = new JButton("View Reservations");
        JButton cancelButton = new JButton("Cancel Reservation");
        reserveButton.setBackground(Color.GREEN);
        viewButton.setBackground(Color.RED);
        cancelButton.setBackground(Color.BLUE);

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow != -1 && selectedSeat != -1) {
                    reserveSeat(selectedRow, selectedSeat);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a seat first!");
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewReservations();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelReservation();
            }
        });

        buttonPanel.add(reserveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);

        panel.add(seatPanel);
        panel.add(buttonPanel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void selectSeat(int row, int seat) {
        if (!seats[row][seat]) {
            if (selectedRow != -1 && selectedSeat != -1) {
                seatButtons[selectedRow][selectedSeat].setBackground(null);
            }
            selectedRow = row;
            selectedSeat = seat;
            seatButtons[row][seat].setBackground(Color.WHITE);
        }
    }

    private static void reserveSeat(int row, int seat) {
        if (seats[row][seat]) {
            JOptionPane.showMessageDialog(null, "Seat " + (char) ('A' + row) + (seat + 1) + " is already reserved!");
        } else {
            String name = JOptionPane.showInputDialog("Enter your name for seat " + (char) ('A' + row) + (seat + 1) + ":");
            if (name != null && !name.trim().isEmpty()) {
                seats[row][seat] = true;
                seatButtons[row][seat].setEnabled(false);
                seatButtons[row][seat].setBackground(null);
                seatButtons[row][seat].setBackground(Color.GREEN);
                seatNames.put(row * 10 + seat + 1, name);
                selectedRow = -1;
                selectedSeat = -1;
                JOptionPane.showMessageDialog(null, "Seat " + (char) ('A' + row) + (seat + 1) + " reserved by " + name + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid name! Please enter a valid name.");
            }
        }
    }

    private static void viewReservations() {
        StringBuilder reservedSeats = new StringBuilder("Reserved Seats:\n");
        for (Map.Entry<Integer, String> entry : seatNames.entrySet()) {
            int seatNumber = entry.getKey();
            String name = entry.getValue();
            reservedSeats.append("Seat ").append((char) ('A' + (seatNumber - 1) / 10)).append(seatNumber % 10).append(" - ").append(name).append("\n");
        }
        if (seatNames.isEmpty()) {
            reservedSeats.append("None");
        }
        JOptionPane.showMessageDialog(null, reservedSeats.toString());
    }

    private static void cancelReservation() {
        String input = JOptionPane.showInputDialog("Enter seat (e.g., A1) to cancel reservation:");
        if (input != null && input.length() == 2) {
            char rowChar = input.charAt(0);
            int seat = Character.getNumericValue(input.charAt(1)) - 1;
            if (rowChar >= 'A' && rowChar <= 'E' && seat >= 0 && seat < 10) {
                int row = rowChar - 'A';
                int seatNumber = row * 10 + seat + 1;
                if (!seats[row][seat]) {
                    JOptionPane.showMessageDialog(null, "Seat " + rowChar + (seat + 1) + " is not reserved!");
                } else {
                    seats[row][seat] = false;
                    seatButtons[row][seat].setEnabled(true);
                    seatButtons[row][seat].setBackground(Color.CYAN);
                    seatNames.remove(seatNumber);
                    JOptionPane.showMessageDialog(null, "Reservation for seat " + rowChar + (seat + 1) + " canceled!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid seat input!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid seat input!");
        }
    }
}