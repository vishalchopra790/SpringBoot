package com.vishal.servicerepo;

import com.vishal.controller.Library;
import com.vishal.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    LibraryRepository libraryRepository;

    public boolean checkBookAlreadyExist(String id) {
        Optional<Library> op = libraryRepository.findById(id);
        return op.isPresent();
    }

    public String buildId(String isbn, int aisle) {
        if (isbn.startsWith("Z")) {
            return "OLD" + isbn + aisle;
        }
        return isbn + aisle;
    }

    public Library getBookByID(String id){

        return libraryRepository.findById(id).get();
    }
}
