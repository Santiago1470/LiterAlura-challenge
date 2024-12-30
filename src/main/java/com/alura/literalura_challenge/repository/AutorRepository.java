package com.alura.literalura_challenge.repository;

import com.alura.literalura_challenge.model.Autor;
import com.alura.literalura_challenge.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombreAutor")
    Optional<Autor> buscarAutorPorNombre(String nombreAutor);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.titulo = :titulo")
    Optional<Libro> buscarLibroPorTitulo(String titulo);

    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> listarLibrosRegistrados();

    List<Autor> findByYearNacimientoLessThanEqualAndYearMuerteGreaterThanEqualOrYearMuerteIsNull(int year, int year2);
}
