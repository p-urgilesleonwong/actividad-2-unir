package com.dwfsg7.msbookscatalogue.api;

import com.dwfsg7.msbookscatalogue.domain.Book;
import com.dwfsg7.msbookscatalogue.repo.BookRepository;
import com.dwfsg7.msbookscatalogue.repo.BookSpecifications;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/books")
@Tag(name = "Book Controller", description = "Microservicio encargado de exponer operaciones para la gestion del catalogo de libros.")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @Operation(
        operationId = "Crear Libro",
        description = "Operacion de escritura para crear un nuevo libro en el catalogo.",
        summary = "Se recibe un libro y se almacena en el catalogo. Se devuelve el libro creado.")
    @ApiResponse(
        responseCode = "201",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
        responseCode = "400",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Datos incorrectos introducidos.")
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        if (book.getId() != null) book.setId(null);
        Book saved = repo.save(book);
        return ResponseEntity.created(URI.create("/books/" + saved.getId())).body(saved);
    }

    @Operation(
        operationId = "Obtener Libro por ID",
        description = "Operacion de lectura para obtener un libro del catalogo por su identificador unico.",
        summary = "Se recibe un identificador unico y se devuelve el libro correspondiente del catalogo.")
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
        responseCode = "404",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "No se ha encontrado el book con el identificador indicado.")
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")
    @ApiResponse(
        responseCode = "400",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "El identificador proporcionado no es valido.")

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        operationId = "Buscar Libros",
        description = "Operacion de lectura para buscar libros en el catalogo segun varios criterios de busqueda.",
        summary = "Se reciben varios parametros de busqueda y se devuelven los libros que coinciden con los criterios indicados.")
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")
    @Parameter(name = "title", description = "Titulo del libro o parte de el.")
    @Parameter(name = "author", description = "Autor del libro o parte de el.")
    @Parameter(name = "category", description = "Categoria del libro.")
    @Parameter(name = "isbn", description = "ISBN del libro.")
    @Parameter(name = "rating", description = "Valoracion del libro (1-5).")
    @Parameter(name = "visible", description = "Visibilidad del libro en el catalogo.")
    @Parameter(name = "publishedFrom", description = "Fecha de publicacion desde la que buscar.")
    @Parameter(name = "publishedTo", description = "Fecha de publicacion hasta la que buscar.")

    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedTo
    ) {
        Specification<Book> spec = Specification.where(BookSpecifications.titleContains(title))
                .and(BookSpecifications.authorContains(author))
                .and(BookSpecifications.categoryEquals(category))
                .and(BookSpecifications.isbnEquals(isbn))
                .and(BookSpecifications.ratingEquals(rating))
                .and(BookSpecifications.visibleEquals(visible))
                .and(BookSpecifications.publishedFrom(publishedFrom))
                .and(BookSpecifications.publishedTo(publishedTo));

        return ResponseEntity.ok(repo.findAll(spec));
    }
    @Operation(
        operationId = "Reemplazar Libro",
        description = "Operacion de escritura para reemplazar un libro existente en el catalogo.",
        summary = "Se recibe un identificador unico y un libro. Se reemplaza el libro existente con el nuevo libro proporcionado.")
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
        responseCode = "404",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "No se ha encontrado el book con el identificador indicado.")
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")
    @ApiResponse(
        responseCode = "400",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "El identificador proporcionado no es valido.")
    @PutMapping("/{id}")
    public ResponseEntity<Book> replace(@PathVariable Long id, @Valid @RequestBody Book book) {
        return repo.findById(id).map(existing -> {
            book.setId(id);
            return ResponseEntity.ok(repo.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        operationId = "Actualizar Libro Parcialmente",
        description = "Operacion de actualizacion parcial para modificar uno o varios campos de un libro en el catalogo.",
        summary = "Se recibe un identificador unico y un mapa con los campos a modificar y sus nuevos valores.")
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
        responseCode = "404",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "No se ha encontrado el book con el identificador indicado.")
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")
    @ApiResponse(
        responseCode = "400",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "El identificador proporcionado no es valido.")

    @PatchMapping("/{id}")
    public ResponseEntity<Book> patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return repo.findById(id).map(existing -> {
            fields.forEach((k, v) -> {
                switch (k) {
                    case "title" -> existing.setTitle((String) v);
                    case "author" -> existing.setAuthor((String) v);
                    case "category" -> existing.setCategory((String) v);
                    case "isbn" -> existing.setIsbn((String) v);
                    case "rating" -> existing.setRating((Integer) v);
                    case "visible" -> existing.setVisible((Boolean) v);
                    case "stock" -> existing.setStock((Integer) v);
                    case "publicationDate" -> existing.setPublicationDate(LocalDate.parse((String) v));
                }
            });
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        operationId = "Eliminar Libro",
        description = "Operacion de escritura para eliminar un libro del catalogo por su identificador unico.",
        summary = "Se recibe un identificador unico y se elimina el libro correspondiente del catalogo.")
    @ApiResponse(
        responseCode = "204",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
        responseCode = "404",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "No se ha encontrado el book con el identificador indicado.")
    @ApiResponse(
        responseCode = "500",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "Ha ocurrido un error inesperado.")
    @ApiResponse(
        responseCode = "400",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "El identificador proporcionado no es valido.")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}