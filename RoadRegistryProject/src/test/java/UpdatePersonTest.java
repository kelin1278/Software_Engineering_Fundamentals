
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdatePersonTest {

    Person person = new Person();
    

@Test
public void testUpdateFailsWhenOldIdNotFound() {
    boolean result = person.updatePersonalDetails(
        "ZZ99notFound",
        "ZZ99notFound",
        "123|Fake St|Melb|Victoria|Australia",
        "01-01-1990"
    );

    assertFalse(result);
}
    @Test
    public void testUpdateAddressFailsForMinor() {
        person.addPerson("29!!kidBB", "200|Young Rd|Geelong|Victoria|Australia", "01-01-2010");

        boolean result = person.updatePersonalDetails(
            "29!!kidBB",
            "29!!kidBB",
            "201|Other Rd|Geelong|Victoria|Australia",
            "01-01-2010"
        );

        assertFalse(result);
    }

@Test
public void testAddValidPersonOnly() {
    String personId = "23!!abcdXY";
    String address = "101|Main St|Melbourne|Victoria|Australia";
    String birthdate = "12-12-1990";

    boolean result = person.addPerson(personId, address, birthdate);

    assertTrue(result, "合法人员应成功添加");
}



    @Test
    public void testChangeBirthdateAndAddressShouldFail() {
        person.addPerson("88&&badDD", "400|Elm St|Bendigo|Victoria|Australia", "01-01-1992");

        boolean result = person.updatePersonalDetails(
            "88&&badDD",
            "88&&badDD",
            "401|Oak St|Bendigo|Victoria|Australia",
            "02-02-1992"
        );

        assertFalse(result);
    }

    @Test
    public void testChangeIDWhenStartsWithEvenDigitShouldFail() {
        person.addPerson("24@@idEE", "500|Main St|Shepparton|Victoria|Australia", "01-01-1990");

        boolean result = person.updatePersonalDetails(
            "24@@idEE",
            "25@@idEE", 
            "500|Main St|Shepparton|Victoria|Australia",
            "01-01-1990"
        );

        assertFalse(result);
    }
}
