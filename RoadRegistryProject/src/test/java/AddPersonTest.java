import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddPersonTest {

    Person person = new Person();

    @Test
    public void testValidPerson() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertTrue(result);
    }

    @Test
    public void testInvalidIDTooShort() {
        boolean result = person.addPerson("56s_dAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }

    @Test
    public void testInvalidAddressWrongState() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|NSW|Australia", "15-11-1990");
        assertFalse(result);
    }

    @Test
    public void testInvalidBirthdateFormat() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "1990-11-15");
        assertFalse(result);
    }

    @Test
    public void testIDMissingSpecialChars() {
        boolean result = person.addPerson("56abcdffAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }
}
