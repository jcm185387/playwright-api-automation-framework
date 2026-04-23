package tests.api;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import services.BookingService;


public class ApiTest {

        private static Playwright playwright;
        private static APIRequestContext request;

        @BeforeAll
        static void setup() {
            playwright = Playwright.create();


            request = playwright.request().newContext(new APIRequest.NewContextOptions()
                    .setBaseURL("https://restful-booker.herokuapp.com"));
        }

        @Test
        @DisplayName("Crear una reserva")
        void createBookingTest() {
            BookingService bookingService = new BookingService(request);

            Map<String, Object> body = createPayload("Juan", "Automation");

            APIResponse response = bookingService.createBooking(body);
            assertEquals(200, response.status());
        }

        @Test
        @DisplayName("Obtener una reserva recién creada")
        void getBookingTest() {
            BookingService bookingService = new BookingService(request);

            // 1. Crear la reserva
            Map<String, Object> body = createPayload("Juan", "Senior");
            APIResponse responseCreate = bookingService.createBooking(body);

            // 2. Extraer el ID de la respuesta JSON
            int bookingId = com.google.gson.JsonParser.parseString(responseCreate.text())
                    .getAsJsonObject().get("bookingid").getAsInt();

            // 3. Consultar la reserva por ese ID
            APIResponse responseGet = bookingService.getBooking(bookingId);
            System.out.println("Cuerpo de la respuesta: " + responseCreate.text());
            assertEquals(200, responseGet.status());
            assertTrue(responseGet.text().contains("Juan"));
        }

        //
        private Map<String, Object> createPayload(String firstName, String lastName) {
            Map<String, Object> body = new HashMap<>();
            body.put("firstname", firstName);
            body.put("lastname", lastName);
            body.put("totalprice", 111);
            body.put("depositpaid", true);

            Map<String, String> dates = new HashMap<>();
            dates.put("checkin", "2026-01-01");
            dates.put("checkout", "2026-01-02");
            body.put("bookingdates", dates);
            body.put("additionalneeds", "Breakfast");

            return body;
        }

        @AfterAll
        static void tearDown() {
            // 5. El dueño del recurso cierra la llave maestra
            if (request != null) {
                request.dispose(); // Cierra el contexto de la API
            }
            if (playwright != null) {
                playwright.close(); // Apaga el motor de Playwright
            }
        }

}
