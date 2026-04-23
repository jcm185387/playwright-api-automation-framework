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

        @Test
        @DisplayName("Actualizar una reserva existente")
        void updateBookingTest() {
            BookingService bookingService = new BookingService(request);

            // 1. Crear reserva inicial
            Map<String, Object> bodyInicial = createPayload("Juan", "Original");
            APIResponse responseCreate = bookingService.createBooking(bodyInicial);
            int bookingId = com.google.gson.JsonParser.parseString(responseCreate.text())
                    .getAsJsonObject().get("bookingid").getAsInt();

            // 2. Preparar datos para actualizar (Cambiamos el apellido y precio)
            Map<String, Object> bodyUpdate = createPayload("Juan", "Actualizado");
            bodyUpdate.put("totalprice", 500);

            // 3. Ejecutar Update
            APIResponse responseUpdate = bookingService.updateBooking(bookingId, bodyUpdate);

            // 4. Validaciones
            assertEquals(200, responseUpdate.status());
            assertTrue(responseUpdate.text().contains("Actualizado"));
            assertTrue(responseUpdate.text().contains("500"));
        }

        @Test
        @DisplayName("Eliminar una reserva recién creada")
        void deleteBookingTest() {
            BookingService bookingService = new BookingService(request);

            // 1. Primero creamos una para tener algo que borrar
            Map<String, Object> body = createPayload("Juan", "ToDelete");
            APIResponse responseCreate = bookingService.createBooking(body);

            // Extraemos el ID
            int bookingId = com.google.gson.JsonParser.parseString(responseCreate.text())
                    .getAsJsonObject().get("bookingid").getAsInt();

            // 2. Ejecutamos el DELETE
            // Usamos el token básico que acepta la API por defecto
            APIResponse responseDelete = request.delete("/booking/" + bookingId, RequestOptions.create()
                    .setHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM="));

            // 3. Validaciones
            assertEquals(201, responseDelete.status(), "El status debería ser 201 Created para delete");

            // 4. Verificación extra: Intentar buscarla y que de 404
            APIResponse responseGet = bookingService.getBooking(bookingId);
            assertEquals(404, responseGet.status(), "La reserva no debería existir más");
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
