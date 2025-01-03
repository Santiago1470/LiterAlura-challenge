package com.alura.literalura_challenge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Long descargas;

    @ManyToOne
    private Autor autor;

    public Libro() {

    }

    public Libro(String titulo, String idioma, Long descargas) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.descargas = descargas;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getDescargas() {
        return descargas;
    }

    public void setDescargas(Long descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return """
                --------- LIBRO ---------
                Titulo: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %d
                """.formatted(this.titulo, this.autor.getNombre(), this.idioma, this.descargas);
    }
}
