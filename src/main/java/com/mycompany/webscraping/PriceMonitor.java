/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.webscraping;

/**
 *
 * @author byAlexLR
 */

 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.PrintWriter;
 import java.net.SocketTimeoutException;
 import java.util.HashSet;
 import java.util.LinkedHashSet;
 import java.util.Optional;
 import java.util.Set;
 import java.util.concurrent.Executors;
 import java.util.concurrent.ScheduledExecutorService;
 import java.util.concurrent.TimeUnit;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 
 import org.json.JSONArray;
 import org.json.JSONObject;
 import org.jsoup.HttpStatusException;
 import org.jsoup.Jsoup;
 import org.jsoup.nodes.Document;
 import org.jsoup.nodes.Element;
 import org.jsoup.select.Elements;

public class PriceMonitor {

    // Logger para registrar mensajes en consola
    private static final Logger LOGGER = Logger.getLogger(PriceMonitor.class.getName());

    // URL del sitio web de e-commerce que se va a scrapea
    private static final String URL_TARGET = "https://books.toscrape.com";
    
    // Archivos donde se guardarán los datos extraídos
    private static final String JSON_FILE = "output/result.json";
    private static final String CSV_FILE = "output/result.csv";

    // Configuración de la conexión HTTP
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final int TIMEOUT = 10_000;

    // Códigos ANSI para colorear la salida en consola
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";

    static {
        // Activar colores en la terminal de Windows (solo para compatibilidad)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "color").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, RED + "Error activando colores ANSI" + RESET, e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Manejo de interrupción con CTRL + C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info(RED + "Ejecución interrumpida manualmente. Cerrando scraper..." + RESET);
        }));

        // Configurar la ejecución periódica del scraper (cada 1 hora)
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        LOGGER.info(() -> GREEN + "Iniciando el monitor de precios..." + RESET);
        scheduler.scheduleAtFixedRate(PriceMonitor::runScraper, 0, 1, TimeUnit.HOURS);
    }

    // Método principal del scraper
    private static void runScraper() {
        try {
            LOGGER.info(() -> BLUE + "Ejecutando el scraping..." + RESET);
            Set<String> paginas = obtenerPaginas(URL_TARGET);
            JSONArray productosArray = new JSONArray();

            for (String pagina : paginas) {
                LOGGER.info(() -> GREEN + "Extrayendo datos de: " + pagina + RESET);
                obtenerHTML(pagina).ifPresent(html -> extraerDatos(html, productosArray));
            }

            guardarDatos(productosArray);
            guardarDatosCSV(productosArray);

            LOGGER.info(() -> GREEN + "Scraping finalizado correctamente." + RESET);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, RED + "Error en la ejecución del scraper" + RESET, e);
        }
    }

    // Obtiene el HTML de la página web
    private static Optional<String> obtenerHTML(String urlString) {
        try {
            Document doc = Jsoup.connect(urlString)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .ignoreHttpErrors(true)
                    .get();
            return Optional.of(doc.html());
        } catch (HttpStatusException e) {
            LOGGER.warning(() -> YELLOW + "Página no disponible: " + urlString + " - Código HTTP: " + e.getStatusCode() + RESET);
        } catch (SocketTimeoutException e) {
            LOGGER.warning(() -> YELLOW + "Tiempo de espera agotado al intentar conectar con: " + urlString + RESET);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, RED + "Error obteniendo el HTML desde: " + urlString + RESET, e);
        }
        return Optional.empty();
    }

    // Obtiene todas las páginas del catálogo
    private static Set<String> obtenerPaginas(String url) {
        Set<String> paginas = new LinkedHashSet<>();
        paginas.add(url);

        try {
            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).get();
            Elements links;
            while (!(links = doc.select("li.next a")).isEmpty()) {
                String nextPage = links.first().absUrl("href");
                if (nextPage.isEmpty() || paginas.contains(nextPage))
                    break;

                paginas.add(nextPage);
                doc = Jsoup.connect(nextPage).userAgent(USER_AGENT).timeout(TIMEOUT).get();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, RED + "Error obteniendo las páginas" + RESET, e);
        }
        return paginas;
    }

    // Extrae datos de productos y los almacena en un JSONArray
    private static void extraerDatos(String html, JSONArray productosArray) {
        try {
            Document document = Jsoup.parse(html);
            Elements productos = document.select("article.product_pod");

            if (productos.isEmpty()) {
                LOGGER.warning(() -> YELLOW + "No se encontraron productos en esta página." + RESET);
                return;
            }

            Set<String> nombresProductos = new HashSet<>();
            for (Element producto : productos) {
                String nombre = producto.select("h3 a").attr("title").trim();
                String precio = producto.select("p.price_color").text().trim();

                if (nombre.isEmpty() || precio.isEmpty()) {
                    LOGGER.warning(() -> YELLOW + "Producto con datos incompletos, se omitirá." + RESET);
                    continue;
                }

                // Evitar productos duplicados
                if (!nombresProductos.add(nombre)) {
                    LOGGER.warning(() -> YELLOW + "Producto duplicado omitido: " + nombre + RESET);
                    continue;
                }

                JSONObject productoJSON = new JSONObject();
                productoJSON.put("nombre", nombre);
                productoJSON.put("precio", precio);
                productosArray.put(productoJSON);
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, RED + "Error en la extracción de datos." + RESET, e);
        }
    }

    // Guarda los datos en formato JSON
    private static void guardarDatos(JSONArray productosArray) {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(JSON_FILE, false)) { // Sobreescribir
            writer.write(productosArray.toString(4));
            LOGGER.info(() -> GREEN + "Datos guardados en JSON correctamente en " + JSON_FILE + RESET);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, RED + "Error al guardar los datos en JSON" + RESET, e);
        }
    }

    // Guarda los datos en formato CSV
    private static void guardarDatosCSV(JSONArray productosArray) {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        boolean archivoExiste = new File(CSV_FILE).exists();

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(CSV_FILE), true))) { // Append
            if (!archivoExiste) {
                writer.println("Nombre,Precio"); // Agregar encabezado si es la primera vez
            }

            productosArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                writer.println(obj.getString("nombre") + "," + obj.getString("precio"));
            });

            LOGGER.info(() -> GREEN + "Datos guardados en CSV correctamente en " + CSV_FILE + RESET);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, RED + "Error al guardar los datos en CSV" + RESET, e);
        }
    }
}