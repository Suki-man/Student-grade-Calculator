import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Raf extends JFrame {

    private JTextField nameField, idField;
    private JTextField sub1Name, sub1Mark, sub2Name, sub2Mark, sub3Name, sub3Mark;
    private JComboBox<String> programBox;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private File historyFile = new File("history.txt");

    public Raf() {
        setTitle("Student Grade Calculator");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set custom gradient background panel
        GradientPanel bgPanel = new GradientPanel();
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        JLabel title = new JLabel("Student Grade Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(200, 10, 300, 30);
        bgPanel.add(title);

        addLabel("Student Name:", 30, 60, bgPanel);
        nameField = addTextField(150, 60, bgPanel);

        addLabel("Programme:", 400, 60, bgPanel);
        String[] programs = {"EEE", "CSE", "LLB"};
        programBox = new JComboBox<>(programs);
        programBox.setBounds(500, 60, 120, 25);
        bgPanel.add(programBox);

        addLabel("Student ID:", 30, 100, bgPanel);
        idField = addTextField(150, 100, bgPanel);

        addLabel("Course 1 Name:", 30, 140, bgPanel);
        sub1Name = addTextField(150, 140, bgPanel);
        addLabel("Course 1 Number:", 350, 140, bgPanel);
        sub1Mark = addTextField(500, 140, bgPanel);

        addLabel("Course 2 Name:", 30, 180, bgPanel);
        sub2Name = addTextField(150, 180, bgPanel);
        addLabel("Course 2 Number:", 350, 180, bgPanel);
        sub2Mark = addTextField(500, 180, bgPanel);

        addLabel("Course 3 Name:", 30, 220, bgPanel);
        sub3Name = addTextField(150, 220, bgPanel);
        addLabel("Course 3 Number:", 350, 220, bgPanel);
        sub3Mark = addTextField(500, 220, bgPanel);

        JButton calcBtn = new JButton("Calculate");
        calcBtn.setBounds(150, 270, 100, 30);
        calcBtn.setBackground(Color.GREEN);
        calcBtn.addActionListener(e -> calculate());
        bgPanel.add(calcBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBounds(250, 270, 100, 30);
        resetBtn.addActionListener(e -> clearFields());
        bgPanel.add(resetBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(350, 270, 100, 30);
        exitBtn.setBackground(Color.RED);
        exitBtn.addActionListener(e -> System.exit(0));
        bgPanel.add(exitBtn);

        JButton hisButton = new JButton("History");
        hisButton.setBounds(450, 270, 100, 30);
        bgPanel.add(hisButton);

        String[] columns = {"Name", "ID", "Total", "CGPA", "Grade", "Performance"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(30, 320, 630, 100);
        bgPanel.add(scrollPane);

        hisButton.addActionListener(e -> showHistory());

        setLocationRelativeTo(null); // center screen
        setVisible(true);
    }

    private void calculate() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();

            if (name.isEmpty() || id.isEmpty()
                    || sub1Mark.getText().trim().isEmpty()
                    || sub2Mark.getText().trim().isEmpty()
                    || sub3Mark.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            double m1 = Double.parseDouble(sub1Mark.getText().trim());
            double m2 = Double.parseDouble(sub2Mark.getText().trim());
            double m3 = Double.parseDouble(sub3Mark.getText().trim());

            if (m1 < 0 || m1 > 100 || m2 < 0 || m2 > 100 || m3 < 0 || m3 > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.");
                return;
            }

            double[] marks = {m1, m2, m3};
            double[] gradePoints = new double[3];

            for (int i = 0; i < 3; i++) {
                double mark = marks[i];
                if (mark >= 90) gradePoints[i] = 4.0;
                else if (mark >= 85) gradePoints[i] = 3.75;
                else if (mark >= 80) gradePoints[i] = 3.50;
                else if (mark >= 75) gradePoints[i] = 3.25;
                else if (mark >= 70) gradePoints[i] = 3.0;
                else if (mark >= 65) gradePoints[i] = 2.75;
                else if (mark >= 60) gradePoints[i] = 2.50;
                else if (mark >= 50) gradePoints[i] = 2.25;
                else gradePoints[i] = 0.0;
            }

            double cgpa = (gradePoints[0] + gradePoints[1] + gradePoints[2]) / 3.0;
            double total = m1 + m2 + m3;

            String grade, performance;
            if (cgpa >= 4.00) {
                grade = "A+";
                performance = "Excellent";
            } else if (cgpa >= 3.75) {
                grade = "A";
                performance = "Excellent";
            } else if (cgpa >= 3.50) {
                grade = "B+";
                performance = "Good";
            } else if (cgpa >= 3.25) {
                grade = "B";
                performance = "Average";
            } else if (cgpa >= 3.00) {
                grade = "C+";
                performance = "Below Average";
            } else if (cgpa >= 2.75) {
                grade = "C";
                performance = "Needs Improvement";
            } else if (cgpa >= 2.50) {
                grade = "D+";
                performance = "Needs Improvement";
            } else if (cgpa >= 2.25) {
                grade = "D";
                performance = "Needs Improvement";
            } else {
                grade = "F";
                performance = "Fail";
            }

            String[] data = {
                name, id, String.valueOf((int) total),
                String.format("%.2f", cgpa), grade, performance
            };
            tableModel.addRow(data);

            try (FileWriter writer = new FileWriter(historyFile, true)) {
                writer.write(String.join(",", data) + "\n");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric marks.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
        }
    }

    private void showHistory() {
        JFrame historyFrame = new JFrame("History Records");
        historyFrame.setSize(600, 400);
        historyFrame.setLayout(new BorderLayout());
        historyFrame.setLocationRelativeTo(null);

        String[] col = {"Name", "ID", "Total", "CGPA", "Grade", "Performance"};
        DefaultTableModel historyModel = new DefaultTableModel(col, 0);
        JTable historyTable = new JTable(historyModel);
        JScrollPane historyScroll = new JScrollPane(historyTable);

        if (historyFile.exists()) {
            try (Scanner scanner = new Scanner(historyFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] row = line.split(",");
                    if (row.length == 6) {
                        historyModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to read history.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No history found.");
        }

        historyFrame.add(historyScroll, BorderLayout.CENTER);
        historyFrame.setVisible(true);
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        sub1Name.setText("");
        sub1Mark.setText("");
        sub2Name.setText("");
        sub2Mark.setText("");
        sub3Name.setText("");
        sub3Mark.setText("");
		tableModel.setRowCount(0);
        programBox.setSelectedIndex(0);
    }

    private void addLabel(String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 130, 25);
        panel.add(label);
    }

    private JTextField addTextField(int x, int y, JPanel panel) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 120, 25);
        panel.add(field);
        return field;
    }

    public static void main(String[] args) {
        new Raf();
    }

    // Custom panel with gradient background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Color color1 = new Color(173, 216, 230); // light blue
            Color color2 = new Color(255, 255, 255); // white
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}