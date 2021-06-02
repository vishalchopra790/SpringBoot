package com.vishal.controller;

import com.vishal.repository.LibraryRepository;
import com.vishal.servicerepo.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LibraryController {
    private static final Logger log = LoggerFactory.getLogger(LibraryController.class);
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    LibraryService libraryService;

    String id;

    @PostMapping("/addbook")
    public ResponseEntity<AddResponse> addBookImplementation(@RequestBody Library library) {
        id = libraryService.buildId(library.getIsbn(), library.getAisle());
        AddResponse add = new AddResponse();
        if (!libraryService.checkBookAlreadyExist(id)) {
            log.info("Books not exist so adding book");
            library.setId(id);
            libraryRepository.save(library);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("unique", id);
            add = new AddResponse();
            add.setId(id);

            add.setMessage("Success book added");
            return new ResponseEntity<AddResponse>(add, httpHeaders, HttpStatus.CREATED);
        } else {
            log.info("Books alredy exists");
            add.setId(id);

            add.setMessage("Book already exists");
            return new ResponseEntity<AddResponse>(add, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/getbooks/{id}")
    public ResponseEntity<Library> getBookDetails(@PathVariable(value = "id") String id) {
        try {
            //Library lib = libraryRepository.findById(id).get();
            Library lib = libraryService.getBookByID(id);
            return new ResponseEntity<Library>(lib, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }


    }

    @GetMapping("getBooks/author")
    public Object getAllBooksByAuthorName(@RequestParam(value = "authorname") String authorname) {

        List<Library> li = libraryRepository.findAllAuthor(authorname);
        if (!(li.size() == 0)) {

            return new ResponseEntity<>(li, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("We don't have any book with this author", HttpStatus.ACCEPTED);
        }


    }

    @PutMapping("/updatebook/{id}")
    public ResponseEntity<Library> updateBook(@PathVariable(name = "id") String id, @RequestBody Library library) {
        try {


            Library existingBook = libraryService.getBookByID(id);
            existingBook.setAisle(library.getAisle());
            existingBook.setAuthor(library.getAuthor());
            existingBook.setBookName(library.getBookName());
            libraryRepository.save(existingBook);
            return new ResponseEntity<Library>(existingBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletebook")
    public ResponseEntity<String> deleteBook(@RequestBody Library library) {
        try {
            //Library book=libraryRepository.findById(library.getId()).get();
            Library book = libraryService.getBookByID(library.getId());
            libraryRepository.delete(book);
            return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("id provide by you invalid", HttpStatus.NOT_FOUND);
        }


    }

    @GetMapping("/allbook")
    public List<Library> displayAllBooks() {

        return libraryRepository.findAllAllBooks();

    }
}
