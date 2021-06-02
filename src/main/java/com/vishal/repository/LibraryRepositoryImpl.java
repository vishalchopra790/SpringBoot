package com.vishal.repository;

import com.vishal.controller.Library;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom{

    @Autowired
    LibraryRepository libraryRepository;
    @Override
    public List<Library> findAllAuthor(String author) {
        List<Library> bookbByAuthor=new ArrayList<>();
        List<Library> books=libraryRepository.findAll();
        for(Library item:books){
            if(item.getAuthor().equalsIgnoreCase(author)){
              bookbByAuthor.add(item);
            }
        }

        return bookbByAuthor;
    }

    @Override
    public List<Library> findAllAllBooks() {
        List<Library> all=libraryRepository.findAll();
        List<Library> llip = new ArrayList<>(all);
        return llip;
    }
}
