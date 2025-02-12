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
        try {
            // URL del sitio web a scrapear
            String urlString = "https://www.ejemplo.com"; // Reemplaza con la URL de tu interés
            URL url = new URL(urlString);

            // Configuración de la conexión HTTP usando HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // Usar GET para obtener el contenido
            connection.setConnectTimeout(5000); // Tiempo de espera para la conexión
            connection.setReadTimeout(5000);    // Tiempo de espera para la lectura de datos
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Añadir un User-Agent para evitar bloqueos por bots

            // Leer el contenido de la página
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Imprimir el HTML obtenido (Opcional)
            System.out.println("HTML Obtenido:");
            System.out.println(content.toString());

            // Usar Jsoup para analizar el HTML
            Document doc = Jsoup.parse(content.toString());

            // Buscar todos los elementos con la clase 'price' (esto depende de la estructura HTML de la página)
            Elements priceElements = doc.select(".price"); // Cambia esto según lo que estés buscando

            // Mostrar los precios encontrados
            System.out.println("\nPrecios encontrados:");
            for (Element priceElement : priceElements) {
                System.out.println(priceElement.text()); // Imprime el texto del elemento (que sería el precio)
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
