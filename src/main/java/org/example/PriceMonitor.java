import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PriceMonitor {

    public static void main(String[] args) {
        String urlString = "https://www.ejemplo.com"; // Reemplazar con una URL válida

        try {
            String htmlContent = obtenerHTML(urlString);
            if (htmlContent != null) {
                extraerDatosProductos(htmlContent);
            } else {
                System.out.println("No se pudo obtener el contenido HTML.");
            }
        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
        }
    }

    // Método para obtener el HTML de la página
    private static String obtenerHTML(String urlString) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error: Código de respuesta HTTP " + responseCode);
                return null;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener el HTML: " + e.getMessage());
            return null;
        }
        return content.toString();
    }

    // Método para extraer nombre y precio de los productos
    private static void extraerDatosProductos(String html) {
        Document doc = Jsoup.parse(html);

        // Ajusta los selectores según la estructura de la página web
        Elements productos = doc.select(".product-item"); // Clase del contenedor del producto

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
        } else {
            System.out.println("\n📌 Productos encontrados:");
            for (Element producto : productos) {
                String nombre = producto.select(".product-title").text(); // Selector del nombre
                String precio = producto.select(".price").text(); // Selector del precio

                // Validar que se obtuvo un nombre y un precio
                if (!nombre.isEmpty() && !precio.isEmpty()) {
                    System.out.println("🛒 Producto: " + nombre);
                    System.out.println("💰 Precio: " + precio);
                    System.out.println("-------------------------");
                }
            }
        }
    }
}
