package getReport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherAPIProgram {

    private static JSONObject weatherData;
    private static String getApiResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        }
        return null;
    }
    public static void main(String[] args) throws IOException {
        
    	String apiUrl = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
        String response = getApiResponse(apiUrl);
    	weatherData = new JSONObject(response);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    String targetDate = scanner.next();
                    double temperature = getTemperatureOnDate(targetDate);
                    if (temperature != Double.MIN_VALUE) {
                        System.out.println("Temperature on " + targetDate + ": " + temperature + " K");
                    } else {
                        System.out.println("Data not available for the provided date.");
                    }
                    break;

                case 2:
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    targetDate = scanner.next();
                    double windSpeed = getWindSpeedOnDate(targetDate);
                    if (windSpeed != Double.MIN_VALUE) {
                        System.out.println("Wind Speed on " + targetDate + ": " + windSpeed + " m/s");
                    } else {
                        System.out.println("Data not available for the provided date.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    targetDate = scanner.next();
                    double pressure = getPressureOnDate(targetDate);
                    if (pressure != Double.MIN_VALUE) {
                        System.out.println("Pressure on " + targetDate + ": " + pressure + " hPa");
                    } else {
                        System.out.println("Data not available for the provided date.");
                    }
                    break;

                case 0:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please choose a valid option.");
                    break;
            }
        }
    }

    private static double getTemperatureOnDate(String targetDate) {
        JSONArray weatherList = weatherData.getJSONArray("list");
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject entry = weatherList.getJSONObject(i);
            String dt_txt = entry.getString("dt_txt");
            if (dt_txt.startsWith(targetDate)) {
                return entry.getJSONObject("main").getDouble("temp");
            }
        }
        return Double.MIN_VALUE;
    }

    private static double getWindSpeedOnDate(String targetDate) {
        JSONArray weatherList = weatherData.getJSONArray("list");
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject entry = weatherList.getJSONObject(i);
            String dt_txt = entry.getString("dt_txt");
            if (dt_txt.startsWith(targetDate)) {
                return entry.getJSONObject("wind").getDouble("speed");
            }
        }
        return Double.MIN_VALUE;
    }

    private static double getPressureOnDate(String targetDate) {
        JSONArray weatherList = weatherData.getJSONArray("list");
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject entry = weatherList.getJSONObject(i);
            String dt_txt = entry.getString("dt_txt");
            if (dt_txt.startsWith(targetDate)) {
                return entry.getJSONObject("main").getDouble("pressure");
            }
        }
        return Double.MIN_VALUE;
    }
}