package org.example;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class Main {
    private static final String API_KEY = "eabb3c4c382635dd5955311f";
    private static final String API_URL_TEMPLATE = "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha a conversão de moeda:");
        System.out.println("1. Dólar para Real");
        System.out.println("2. Real para Dólar");
        System.out.println("3. Euro para Real");
        System.out.println("4. Real para Euro");
        System.out.println("5. Dólar para Euro");
        System.out.println("6. Euro para Dólar");

        int opcao = scanner.nextInt();
        System.out.println("Digite o valor a ser convertido:");
        double valor = scanner.nextDouble();

        String fromCurrency = "", toCurrency = "";
        switch (opcao) {
            case 1:
                fromCurrency = "USD";
                toCurrency = "BRL";
                break;
            case 2:
                fromCurrency = "BRL";
                toCurrency = "USD";
                break;
            case 3:
                fromCurrency = "EUR";
                toCurrency = "BRL";
                break;
            case 4:
                fromCurrency = "BRL";
                toCurrency = "EUR";
                break;
            case 5:
                fromCurrency = "USD";
                toCurrency = "EUR";
                break;
            case 6:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            default:
                System.out.println("Opção inválida!");
                System.exit(0);
        }

        double taxaDeCambio = obterTaxaDeCambio(fromCurrency, toCurrency);
        double valorConvertido = valor * taxaDeCambio;

        System.out.printf("O valor convertido é: %.2f %s%n", valorConvertido, toCurrency);
    }

    private static double obterTaxaDeCambio(String fromCurrency, String toCurrency) {
        try {
            String urlStr = String.format(API_URL_TEMPLATE, API_KEY, fromCurrency, toCurrency);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            // Verificar a resposta JSON
            System.out.println("Resposta JSON: " + jsonResponse);

            if (!jsonResponse.get("result").getAsString().equals("success")) {
                throw new RuntimeException("Erro na resposta da API: " + jsonResponse.get("error-type").getAsString());
            }

            double taxa = jsonResponse.get("conversion_rate").getAsDouble();

            return taxa;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao obter taxa de câmbio");
            System.exit(1);
            return 0;
        }
    }
}