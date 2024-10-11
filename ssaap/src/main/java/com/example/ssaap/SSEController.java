package com.example.ssaap;

import com.example.ssaap.Producto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SSEController {

    private final List<Producto> productos = new ArrayList<>();

    public SSEController() {
        productos.add(new Producto("Artículo 1", 10.0, 5));
        productos.add(new Producto("Artículo 2", 20.0, 5));
        productos.add(new Producto("Artículo 3", 30.0, 5));

        new Thread(this::nuevoStock).start();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendEvents() {
        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {
            try {
                while (true) {
                    for (Producto producto : productos) {
                        String data = "nombre: " + producto.getNombre() +
                                " - precio: " + producto.getPrecio() +
                                " - stock: " + producto.getStock();
                        emitter.send(SseEmitter.event().data(data));
                    }
                    Thread.sleep(5000); // Esperar antes de la próxima actualización
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    private void nuevoStock() {
        try {
            while (true) {
                for (int i = 0; i < productos.size(); i++) {
                    int numero = (int)(Math.random() * 10 + 1);
                    int nuevoStock = productos.get(i).getStock() + numero;
                    productos.set(i, new Producto(productos.get(i).getNombre(), productos.get(i).getPrecio(), nuevoStock));
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
