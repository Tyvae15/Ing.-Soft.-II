/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.laboratorio_02;

/**
 *
 * @author jacks
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InvestmentPortfolio {

    private String userId;
    private List<Transaction> transactions;
    private Map<String, Double> fundBalances;

    public InvestmentPortfolio(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.fundBalances = new HashMap<>();
    }

    // Método general para procesar transacciones (compra o venta)
    private void processTransactionCommon(String fundCode, double amount, String transactionType) {
        // Validaciones generales para ambas transacciones
        if (fundCode == null || fundCode.isEmpty()) {
            throw new IllegalArgumentException("El código del fondo no puede estar vacío");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        // Verificar si el fondo existe
        if (!isFundValid(fundCode)) {
            throw new IllegalArgumentException("El fondo no existe: " + fundCode);
        }

        // Verificar fondos suficientes en caso de ser una venta
        if (transactionType.equals("SELL")) {
            Double currentBalance = fundBalances.getOrDefault(fundCode, 0.0);
            if (currentBalance < amount) {
                throw new IllegalArgumentException("Saldo insuficiente. Balance actual: " + currentBalance);
            }
        }

        // Crear la transacción
        String transactionId = UUID.randomUUID().toString();
        Date transactionDate = new Date();
        Transaction transaction = new Transaction(
                transactionId,
                userId,
                fundCode,
                transactionType,
                amount,
                transactionDate);

        // Registrar la transacción
        transactions.add(transaction);

        // Actualizar el balance del fondo
        if (transactionType.equals("BUY")) {
            fundBalances.put(fundCode, fundBalances.getOrDefault(fundCode, 0.0) + amount);
        } else if (transactionType.equals("SELL")) {
            fundBalances.put(fundCode, fundBalances.get(fundCode) - amount);
        }

        // Registrar la transacción en la base de datos
        saveTransactionToDatabase(transaction);

        // Notificar al usuario
        sendNotificationToUser(transactionType.equals("BUY")
                ? "Se ha realizado una compra por $" + amount + " en el fondo " + fundCode
                : "Se ha realizado un rescate por $" + amount + " del fondo " + fundCode);

        System.out.println("Transacción de " + transactionType.toLowerCase() + " procesada exitosamente. ID: " + transactionId);
    }

    // Método para procesar una compra
    public void processBuyTransaction(String fundCode, double amount) {
        processTransactionCommon(fundCode, amount, "BUY");
    }

    // Método para procesar una venta
    public void processSellTransaction(String fundCode, double amount) {
        processTransactionCommon(fundCode, amount, "SELL");
    }

    private boolean isFundValid(String fundCode) {
        // Simulación de validación de fondo
        return fundCode.startsWith("FUND");
    }

    private void saveTransactionToDatabase(Transaction transaction) {
        // Simulación de guardado en base de datos
        System.out.println("Guardando transacción en la base de datos: " + transaction.getId());
    }

    private void sendNotificationToUser(String message) {
        // Simulación de envío de notificación
        System.out.println("Notificación para usuario " + userId + ": " + message);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Double> getFundBalances() {
        return fundBalances;
    }

}
