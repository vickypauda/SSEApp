package com.example.ssaap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SSEController {


    @GetMapping("/sse")
    public SseEmitter streamEvents() {

        // SseEmitter para gestionar la conexión
        SseEmitter emitter = new SseEmitter();

        // Ejecuta la emisión de eventos de manera asíncrona
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Enviar varios eventos simulados al cliente
                for (int i = 0; i < 10; i++) {
                    emitter.send(SseEmitter.event()
                            .name("evento")
                            .data("Mensaje desde el servidor: " + i));
                    TimeUnit.SECONDS.sleep(1); // Simula un retraso entre eventos
                }
                emitter.complete(); // Completa la transmisión
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;  // Devuelve el SseEmitter al cliente
    }

}
