package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "5ff16b254b4cca81a8d02be4";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Elija una opción:");
            System.out.println("1. De dólar a peso argentino");
            System.out.println("2. De peso argentino a dólar");
            System.out.println("3. De dólar a real brasileño");
            System.out.println("4. De real brasileño a dólar");
            System.out.println("5. De dólar a peso colombiano");
            System.out.println("6. De peso colombiano a dólar");
            System.out.println("0. Salir");

            System.out.print("Ingrese el número de la opción deseada: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume la nueva línea pendiente después de nextInt()

            if (option == 0) {
                System.out.println("Programa finalizado.");
                break;
            }

            String fromCurrency = "USD";
            String toCurrency = "";
            switch (option) {
                case 1:
                    toCurrency = "ARS";
                    break;
                case 2:
                    fromCurrency = "ARS";
                    toCurrency = "USD";
                    break;
                case 3:
                    toCurrency = "BRL";
                    break;
                case 4:
                    fromCurrency = "BRL";
                    toCurrency = "USD";
                    break;
                case 5:
                    toCurrency = "COP";
                    break;
                case 6:
                    fromCurrency = "COP";
                    toCurrency = "USD";
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, ingrese una opción válida.");
                    continue;
            }

            System.out.println("Ingrese la cantidad a convertir: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume la nueva línea pendiente después de nextDouble()

            double rate = getExchangeRate(fromCurrency, toCurrency);
            if (rate != -1) {
                double convertedAmount = amount * rate;
                System.out.printf("La cantidad convertida es: %.2f %s%n", convertedAmount, toCurrency);
            } else {
                System.out.println("Error al obtener la tasa de cambio.");
            }
        }

        scanner.close(); // Cerrar el scanner al finalizar
    }

    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String urlStr = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", API_KEY, fromCurrency);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonObject.getAsJsonObject("conversion_rates").get(toCurrency).getAsDouble();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
