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
        String urlString = "https://www.ejemplo.com"; // Reemplaza con la URL de tu interés
        try {
            String htmlContent = obtenerHTML(urlString);
            if (htmlContent != null) {
                extraerPrecios(htmlContent);
            } else {
                System.out.println("No se pudo obtener el contenido HTML.");
            }
        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
        }
    }

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

    private static void extraerPrecios(String html) {
        Document doc = Jsoup.parse(html);
        Elements priceElements = doc.select(".price"); // Ajustar según la estructura de la web

        if (priceElements.isEmpty()) {
            System.out.println("No se encontraron precios.");
        } else {
            System.out.println("\nPrecios encontrados:");
            for (Element priceElement : priceElements) {
                System.out.println(priceElement.text());
            }
        }
    }
}
