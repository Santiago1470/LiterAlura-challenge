package com.alura.literalura_challenge.service;

public interface IConvertidorDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
