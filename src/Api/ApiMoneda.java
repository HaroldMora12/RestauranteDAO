package Api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiMoneda {

    private static final String API_KEY = "6f36b3a6e2637519f82e1dec";

    public static double convertir(String from, String to, double amount) {

        try {

            String direccion = "https://v6.exchangerate-api.com/v6/"
                    + API_KEY + "/latest/" + from;

            URL url = new URL(direccion);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream())
            );

            String linea;
            StringBuilder respuesta = new StringBuilder();

            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }

            br.close();

            String json = respuesta.toString();

            // Verificar que la respuesta fue exitosa
            if (!json.contains("\"result\":\"success\"")) {
                System.out.println("Error en la API");
                return 0;
            }

            // Buscar la tasa de la moneda destino
            String buscar = "\"" + to + "\":";
            int index = json.indexOf(buscar);

            if (index == -1) {
                return 0;
            }

            String sub = json.substring(index + buscar.length());
            String numero = sub.split(",")[0];

            double tasa = Double.parseDouble(numero);

            return amount * tasa;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}