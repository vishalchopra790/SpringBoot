package com.vishal;

import com.vishal.controller.Library;
import com.vishal.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class SprintRestServicesApplication implements CommandLineRunner {
@Autowired
LibraryRepository libraryRepository;
	public static void main(String[] args) {
		SpringApplication.run(SprintRestServicesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*libraryRepository.findAll();
		Library lib=libraryRepository.findById("fdsefr343").get();
		System.out.println(lib.getAuthor());
		Library en=new Library();
		en.setAisle(12345);
		en.setAuthor("Vishal Chopra");
		en.setBookName("Apex");
		en.setId("rty65412345");
		en.setIsbn("rty654");
		libraryRepository.save(en);
		List<Library> allrecords=libraryRepository.findAll();
		for(Library item:allrecords){
			System.out.println(item.getAuthor());*/
/*		}

libraryRepository.delete(en);*/

	}
}
