package student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DataReader {
    private static int reportCounter;

    static {
        retrieveLastIDs();
    }

    public Set<String> getSubmittedAssessmentIDs(String currentUserID) {
        Set<String> submittedAssessmentIDs = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 12) {
                    String studentID = fields[2];
                    String assessmentID = fields[4];
                    if (studentID.equals(currentUserID)) {
                        submittedAssessmentIDs.add(assessmentID);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return submittedAssessmentIDs;
    }

    public static String getUserIntakeFromLogin(String currentUserID) {
        try {
            File studentFile = new File("src/main/resources/database/student.txt");
            Scanner studentScanner = new Scanner(studentFile);

            while (studentScanner.hasNextLine()) {
                String line = studentScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(currentUserID)) {
                    return parts[3].trim();
                }
            }
            studentScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveSubmissionDataToFile(String data) {
        try (FileWriter writer = new FileWriter("src/main/resources/database/report.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAssessmentName(String assessmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11 && fields[0].equals(assessmentID)) {
                    return fields[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSupervisorID(String assessmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11 && fields[0].equals(assessmentID)) {
                    return fields[5];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSecondMarkerID(String assessmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11 && fields[0].equals(assessmentID)) {
                    return fields[6];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int generateReportID() {
        reportCounter++;
        return reportCounter;
    }

    private static void retrieveLastIDs() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/database/report.txt"));

            if (!lines.isEmpty()) {
                String lastLine = lines.get(lines.size() - 1);
                String[] values = lastLine.split(",");

                if (values.length >= 1) {
                    reportCounter = Integer.parseInt(values[0].trim());
                } else {
                    System.err.println("Error: Insufficient values in the last line of report.txt");
                }
            } else {
                System.err.println("Error: Empty file report.txt");
            }
        } catch (IOException e) {
            System.err.println("Error reading report.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing IDs in report.txt: " + e.getMessage());
        }
    }

}
