package MockExam.Calculator.Controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping(path = "/calculate")

public class CalController {
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> postNumber(@RequestBody String payload,
            @RequestHeader("User-Agent") String userAgent) {
        // convert payload from string to json object
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder()
                    .add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }

        // getting values from body using key (ref to inspect>network>payload) in
        // desired format
        int oper1 = body.getInt("oper1");
        int oper2 = body.getInt("oper2");
        String ops = body.getString("ops");
        // cal function
        int result = 0;

        if (ops == "plus") {
            result = oper1 + oper2;
        } else if (ops == "minus") {
            result = oper1 - oper2;
        } else if (ops == "divide") {
            result = oper1 / oper2;
        }

        else if (ops == "multiply") {
            result = oper1 * oper2;
        }
        // building response object cus send back response as json object
        JsonObject response = Json.createObjectBuilder()
                .add("result", result)
                .add("timestamp", Long.toString(System.currentTimeMillis()))
                .add("userAgent", userAgent)
                .build();
        return ResponseEntity.ok(response.toString());

    }
}
