package es.bytescolab.ms_customers.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    @PostMapping("/register")
    public ResponseEntity<?> register() {

        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            // Llamamos al servicio que devuelve JSON con access y refresh tokens
            //String response = authService.login(username, password);

            // Convertimos el string JSON a Map para devolverlo como ResponseEntity
            //return ResponseEntity.ok(new JSONObject(response).toMap());
            return null;
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Collections.singletonMap("error", "Credenciales inválidas"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Error interno del servidor"));
        }
    }


}
