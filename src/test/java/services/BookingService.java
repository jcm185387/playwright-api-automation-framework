package services;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;

public class BookingService {
    private APIRequestContext request;

    public BookingService(APIRequestContext request) {
        this.request = request;
    }

    // CREATE
    public APIResponse createBooking(Map<String, Object> payload) {
        return request.post("/booking", RequestOptions.create().setData(payload));
    }

    // READ (Obtener una reserva específica por ID)
    public APIResponse getBooking(int id) {
        return request.get("/booking/" + id);
    }

    public APIResponse updateBooking(int id, Map<String, Object> payload) {
        return request.put("/booking/" + id, RequestOptions.create()
                .setData(payload)
                .setHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM="));
    }

    // DELETE (Requiere un Token o Auth, pero para el ejemplo básico:)
    public APIResponse deleteBooking(int id) {
        return request.delete("/booking/" + id, RequestOptions.create()
                .setHeader("Cookie", "token=auth_token_here")); // Nota: En un framework real, el token se genera dinámicamente
    }
}