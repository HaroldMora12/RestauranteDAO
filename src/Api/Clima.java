package Api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class Clima {

    private static final String API_KEY = "f0c7b512e38dfe3306063962acf85725";
    private static final String CIUDAD = "TULÚA";

    public static String obtenerClima() {
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + CIUDAD + "&appid=" + API_KEY + "&units=metric&lang=es";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JSONObject json = new JSONObject(response.toString());

            double temp = json.getJSONObject("main").getDouble("temp");
            String descripcion = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");

            return "Clima en " + CIUDAD + ": " + temp + "°C, " + descripcion;

        } catch (Exception e) {
            e.printStackTrace();
            return "No se pudo obtener el clima";
        }
    }
}

