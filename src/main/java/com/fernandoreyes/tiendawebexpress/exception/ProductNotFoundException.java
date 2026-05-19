package com.fernandoreyes.tiendawebexpress.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super("No se encontro el producto con id " + productId + ".");
    }
}
