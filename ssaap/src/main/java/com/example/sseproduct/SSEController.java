package com.example.sseproduct;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SSEController {
    private final List<Producto> productos = new ArrayList<>();

    public SSEController() {
        // Inicializar algunos artículos
        productos.add(new Producto("Artículo 1", 10.0, 5));
        productos.add(new Producto("Artículo 2", 20.0, 5));
        productos.add(new Producto("Artículo 3", 30.0, 5));

        // Simular actualizaciones de precios en un hilo separado
        new Thread(this::nuevoStock).start();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void sendEvents(PrintWriter out) {
        try {
            while (true) {
                for (Producto producto : productos) {
                    out.write("data: nombre:" + producto.getNombre() + " - precio:" + producto.getPrecio() + " - stock:" + producto.getStock() + "\n\n");
                    out.flush();
                }
                Thread.sleep(5000); // Esperar antes de la próxima actualización
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nuevoStock() {
        try {
            while (true) {
                for (int i = 0; i < productos.size(); i++) {
                    int numero = (int)(Math.random()*10+1);
                    int nuevoStock = productos.get(i).getStock()+numero;
                    productos.set(i, new Producto(productos.get(i).getNombre(), productos.get(i).getPrecio(),nuevoStock)); // Asegúrate de que el precio no sea negativo
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
