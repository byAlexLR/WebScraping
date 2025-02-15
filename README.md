# 🖥️ Web Scraping - Alejandro & Larian

## 📌 Cronograma del Proyecto: Monitorización de Precios

| **Sesión** | **Actividades**                                                                                       | **Duración Estimada** | **Hecho** |
|------------|-------------------------------------------------------------------------------------------------------|-----------------------|-----------------------|
| **Sesión 1** | - Configuración del entorno de desarrollo. <br> - Creación de la estructura básica del proyecto. <br> - Realización de solicitudes HTTP. <br> - Verificación de la obtención de HTML. | 6 Feb | **Hecho** |
| **Sesión 2** | - Implementación de lógica para parsear HTML. <br> - Uso de librerías para extracción de datos. <br> - Verificación de extracción de datos. | 12 Feb | **Hecho** |
| **Sesión 3** | - Almacenamiento de datos (CSV/JSON). <br> - Implementación de actualización periódica. <br> - Verificación del almacenamiento y actualización de datos. | 13 Feb | **Hecho** |
| **Sesión 4** | - Integración de todas las partes del proyecto. <br> - Manejo de errores y validaciones. <br> - Pruebas de flujo completo y simulación de fallos. | 19 Feb | **Hecho** |
| **Sesión 5** | - Documentación en el archivo `README.md`. <br> - Pruebas finales y presentación. <br> - Demostración del scraper en funcionamiento. | 20 Feb | **Hecho** |

## 📂 Estructura del Proyecto
```
WebScraping/
│── README.md                    # Documentación del proyecto
│── pom.xml                      # Dependencias
│── src/main/java/com/mycompany/webscraping   
│   ├── PriceMonitor.java        # Código fuente del proyecto
│── output/                      
│   ├── result.json              # Archivo que guarda los resultados en JSON
│   ├── result.csv               # Archivo que guarda los resultados en CSV
```

## 🛠️ Tecnologías Utilizadas
- **Java** como lenguaje principal.
- **Maven** para la gestión de dependencias.
- **Jsoup** para el web scraping.
- **JSON y CSV** para almacenamiento de datos.
- **Logger** para manejo de logs y excepciones.

## 🚀 Instrucciones para Ejecutarlo
1. Clonar el repositorio:  
   ```sh
   git clone https://github.com/byAlexLR/WebScraping.git
   ```
2. Navegar al directorio del proyecto:
   ```sh
   cd webscraping
   ```
3. Compilar y ejecutar con Maven:
   ```sh
   mvn clean install
   mvn exec:java
   ```
4. Los datos extraídos se almacenarán en:
   - **JSON:** `output/result.json`
   - **CSV:** `output/result.csv`

## 🤖 Uso de IA en el Proyecto
Para desarrollar este proyecto, hemos utilizado inteligencia artificial de generación de código, específicamente ChatGPT. La IA fue utilizada en los siguientes aspectos:
- **Generación de código**: Se solicitó asistencia para estructurar el código de scraping, manejo de excepciones y almacenamiento en JSON/CSV.
- **Optimización de código**: Se refinó el manejo de errores y la eficiencia del proceso de scraping.
- **Depuración**: Se usó IA para diagnosticar errores en la configuración de Maven y la integración de dependencias.

Hemos seguido un enfoque de *Pair Programming*, donde uno de nosotros interactuaba con la IA mientras el otro revisaba y ajustaba la lógica según las necesidades del proyecto.

## 📊 Pruebas y Resultados
- Se realizaron pruebas finales verificando que el scraper funciona correctamente.
- Se comprobaron diferentes escenarios, incluyendo páginas sin productos y errores de conexión.
- Se aseguró que los archivos JSON y CSV se actualicen correctamente cada hora.

---
Este proyecto ha sido realizado con la ayuda de la inteligencia artificial, cumpliendo así los requisitos especificados en el módulo de Digitalización (1º CFGS - Desarrollo de Aplicaciones Multiplataforma). 🧑🏻‍💻
