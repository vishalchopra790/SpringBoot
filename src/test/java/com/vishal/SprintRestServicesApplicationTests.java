package com.vishal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishal.controller.AddResponse;
import com.vishal.controller.Library;
import com.vishal.controller.LibraryController;
import com.vishal.repository.LibraryRepository;
import com.vishal.servicerepo.LibraryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SprintRestServicesApplicationTests {
    @MockBean
    @Autowired
    LibraryService libraryService;

    @Autowired
    LibraryController con;

    @MockBean
    LibraryRepository libraryRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void checkBuildIDLogic() {
        LibraryService li = new LibraryService();
        String id = li.buildId("ZMAN", 24);
        assertEquals(id, "OLDZMAN24");
        String id1 = li.buildId("MAN", 24);
        assertEquals(id1, "MAN24");

    }

    @Test
    public void checkAddBookTest() {
        Library lib = buildLibrary();
        when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
        when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(true);
        ResponseEntity response = con.addBookImplementation(buildLibrary());
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        AddResponse addResponse = (AddResponse) response.getBody();
        addResponse.getId();
        assertEquals(addResponse.getId(), lib.getId());
        assertEquals(addResponse.getMessage(), "Book already exists");

        System.out.println(addResponse.getId());
        System.out.println(addResponse.getMessage());

    }

    @Test
    public void addBookControllerTest() throws Exception {
        Library lib = buildLibrary();
        ObjectMapper object = new ObjectMapper();
        String jsdn = object.writeValueAsString(lib);
        when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
        when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
        when(libraryRepository.save(ArgumentMatchers.any())).thenReturn(lib);
        this.mockMvc.perform(post("/addbook").contentType(MediaType.APPLICATION_JSON)
                .content(jsdn)).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.id").value(lib.getId()))
                .andExpect(jsonPath("$.message").value("Success book added"));

    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        List<Library> li = new ArrayList<>();
        li.add(buildLibrary());
        li.add(buildLibrary());
        when(libraryRepository.findAllAuthor(any())).thenReturn(li);
        this.mockMvc.perform(get("/getBooks/author").param("authorname", "Vishal")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].id").value("vj4586"));
    }

    @Test
    public void updateBookTest() throws Exception {
        Library lib = buildLibrary();
        ObjectMapper object = new ObjectMapper();
        String jsdn = object.writeValueAsString(updateLibrary());
        when(libraryService.getBookByID(any())).thenReturn(lib);
        this.mockMvc.perform(put("/updatebook/" + lib.getId()).contentType(MediaType.APPLICATION_JSON).content(jsdn)
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(jsonPath("$.bookName").value("Sahil"))
                .andExpect(content().json("{\"bookName\":\"Sahil\",\"id\":\"vj4586\",\"isbn\":\"vj\",\"aisle\":45889,\"author\":\"sahil\"}"));
    }

    @Test
    public void deleteBookTest() throws Exception {
        Library lib = buildLibrary();
/*        ObjectMapper ob=new ObjectMapper();
        String json=ob.writeValueAsString(deleteLibrary());
        when(libraryService.getBookByID(any())).thenReturn(lib);
        doNothing().when(libraryRepository).delete(buildLibrary());
        this.mockMvc.perform(delete("/deletebook/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated()).andExpect(content().string("Book is deleted"));*/
/*        when(libraryService.getBookByID(lib.getId())).thenReturn(lib);
        doNothing().when(libraryRepository).delete(buildLibrary());
        this.mockMvc.perform(delete("/deletebook/").contentType(MediaType.APPLICATION_JSON).content("{ \"id\" :\"fdsefr343\"}"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated()).andExpect(content().string("Book is deleted"));*/
        when(libraryService.getBookByID(lib.getId())).thenReturn(lib);
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/deletebook/")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"id\" :\"vj458\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void getAllBooksTest() throws Exception {
        List<Library> li = new ArrayList<>();
        li.add(buildLibrary());
        li.add(buildLibrary());
        when(libraryRepository.findAllAllBooks()).thenReturn(li);
        this.mockMvc.perform(get("/allbook")).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    }

    @Test
    public void getBookByIDTest() throws Exception {
        Library lib=buildLibrary();

        when(libraryService.getBookByID(any())).thenReturn(lib);
        this.mockMvc.perform(get("/getbooks/"+lib.getId())).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    }

    private Library deleteLibrary() {
        Library library = new Library();
        library.setId("jk");
        return library;
    }

    public Library buildLibrary() {
        Library library = new Library();
        library.setBookName("Vishal Ji");
        library.setAisle(4586);
        library.setId("vj4586");
        library.setIsbn("vj");
        library.setAuthor("Vishal");
        return library;

    }

    public Library updateLibrary() {
        Library library = new Library();
        library.setBookName("Sahil");
        library.setAisle(45889);

        library.setAuthor("sahil");
        return library;

    }
}
