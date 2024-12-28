package com.alura.literalura_challenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        @JsonAlias("count") Long cantidad,
        @JsonAlias("next") String urlSiguiente,
        @JsonAlias("previous") String urlAnterior,
        @JsonAlias("results") List<DatosLibro> datosLibros
) {
}
