import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class Person {

    private static final String FILE_PATH = "person_data.txt";

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
}
