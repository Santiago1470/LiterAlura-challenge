package com.alura.literalura_challenge.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String nombre;

    private Integer yearNacimiento;
    private Integer yearMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {

    }

    public Autor(String nombre, Integer yearNacimiento, Integer yearMuerte, List<Libro> libros) {
        this.nombre = nombre;
        this.yearNacimiento = yearNacimiento;
        this.yearMuerte = yearMuerte;
        this.libros = libros;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getYear_nacimiento() {
        return yearNacimiento;
    }

    public void setYear_nacimiento(Integer yearNacimiento) {
        this.yearNacimiento = yearNacimiento;
    }

    public Integer getYear_muerte() {
        return yearMuerte;
    }

    public void setYear_muerte(Integer yearMuerte) {
        this.yearMuerte = yearMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public StringBuilder mostrarLibros() {
        StringBuilder lista = new StringBuilder("[");
        if (!lista.isEmpty()) {
            this.libros
                    .forEach(l -> lista.append(l.getTitulo()).append(", "));
            lista.delete(lista.length() - 2, lista.length());
        }
        lista.append("]");

        return lista;
    }

    @Override
    public String toString() {
        return """
                Autor: %s
                Fecha de nacimiento: %s
                Fecha de fallecimiento: %s
                Libros: %s
                """.formatted(nombre, yearNacimiento, yearMuerte, mostrarLibros());
    }
}
