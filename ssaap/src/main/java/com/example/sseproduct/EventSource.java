package com.example.sseproduct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.util.List;

@RestController
public class EventSource<T> {
    private final List<Producto> productos;

    public EventSource(List<Producto> productos) {
        this.productos = productos;
    }

    @GetMapping(value = "/events-source", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Void> sendEvents(PrintWriter out) {
        try {
            for (Producto producto : productos) {
                out.write("data: nombre:" + producto.getNombre() + " - precio:" + producto.getPrecio() + " - stock:" + producto.getStock() + "\n\n");
                out.flush();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONNECTION, "keep-alive").build();
    }
}
