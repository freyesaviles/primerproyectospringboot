package com.fernandoreyes.tiendawebexpress.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName, int availableStock) {
        super("No hay stock suficiente para " + productName + ". Disponible: " + availableStock + " unidad(es).");
    }
}
