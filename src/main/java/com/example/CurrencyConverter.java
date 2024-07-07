package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "5ff16b254b4cca81a8d02be4"; // Clave API proporcionada
    private static final String[] CURRENCIES = {"ARS", "BOB", "BRL", "CLP"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese la moneda de origen (USD): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.println("Ingrese la moneda de destino (ARS, BOB, BRL, CLP): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.println("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();

        double rate = getExchangeRate(fromCurrency, toCurrency);
        if (rate != -1) {
            double convertedAmount = amount * rate;
            System.out.printf("La cantidad convertida es: %.2f %s%n", convertedAmount, toCurrency);
        } else {
            System.out.println("Error al obtener la tasa de cambio.");
        }
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
