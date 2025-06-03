import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Person {

    private static final String FILE_PATH = "person_data.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public boolean addPerson(String personID, String address, String birthdate) {
        // Validate personID: 10 characters, 2 digits (2–9), 2 special chars (3–8), ends with 2 uppercase
        if (!Pattern.matches("^[2-9]{2}[\\w\\W]{6}[A-Z]{2}$", personID)) {
            return false;
        }

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = personID.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }
        if (specialCount < 2) return false;

        // Validate address: StreetNo|Street|City|Victoria|Country
        String[] parts = address.split("\\|");
        if (parts.length != 5 || !parts[3].equalsIgnoreCase("Victoria")) {
            return false;
        }

        // Validate birthdate: DD-MM-YYYY
        if (!Pattern.matches("^\\d{2}-\\d{2}-\\d{4}$", birthdate)) {
            return false;
        }

        // Passed all checks – write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(personID + "|" + address + "|" + birthdate + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean updatePersonalDetails(String oldPersonID, String newPersonID, String address, String birthdate) {
        // Validate newPersonID
        if (!Pattern.matches("^[2-9]{2}.{6}[A-Z]{2}$", newPersonID)) return false;
        int specialCountID = 0;
        for (int i = 2; i <= 7; i++) {
            char c = newPersonID.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCountID++;
        }
        if (specialCountID < 2) return false;

        // Validate address
        String[] addressParts = address.split("\\|");
        if (addressParts.length != 5 || !addressParts[3].equalsIgnoreCase("Victoria")) return false;

        // Validate birthdate
        if (!Pattern.matches("^\\d{2}-\\d{2}-\\d{4}$", birthdate)) return false;

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length >= 3 && parts[0].equals(oldPersonID)) {
                    found = true;
                    String existingAddress = parts[1];
                    String existingBirthdate = parts[2];

                    boolean birthdateChanged = !existingBirthdate.equals(birthdate);
                    boolean addressChanged = !existingAddress.equals(address);
                    boolean personIdChanged = !oldPersonID.equals(newPersonID);

                    // Rule 3: If ID starts with an even digit, ID cannot be changed
                    char firstChar = oldPersonID.charAt(0);
                    if (Character.isDigit(firstChar)) {
                        int digit = Character.getNumericValue(firstChar);
                        if (digit % 2 == 0 && personIdChanged) {
                            System.out.println("Error: Cannot change ID if first digit is even.");
                            return false;
                        }
                    }

                    // Rule 2: If birthdate is changed, other fields must remain unchanged
                    if (birthdateChanged && (addressChanged || personIdChanged)) {
                        System.out.println("Error: Changing birthdate must not change other fields.");
                        return false;
                    }

                    // Rule 1: If under 18, cannot change address
                    if (addressChanged) {
                        try {
                            LocalDate date = LocalDate.parse(existingBirthdate, DATE_FORMATTER);
                            int age = Period.between(date, LocalDate.now()).getYears();
                            if (age < 18) {
                                System.out.println("Error: Cannot change address for persons under 18.");
                                return false;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Error parsing birthdate: " + e.getMessage());
                            lines.add(line);
                            continue;
                        }
                    }

                    lines.add(newPersonID + "|" + address + "|" + birthdate);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!found) {
            System.out.println("Error: Person ID not found.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
