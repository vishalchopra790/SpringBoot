package com.vishal.repository;


import com.vishal.controller.Library;

import java.util.List;

public interface LibraryRepositoryCustom {
    List<Library> findAllAuthor(String author);
    List<Library> findAllAllBooks();
}
