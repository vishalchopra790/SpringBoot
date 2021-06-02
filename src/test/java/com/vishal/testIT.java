package com.vishal;



import com.vishal.controller.Library;
import com.vishal.servicerepo.LibraryService;

import io.restassured.path.json.JsonPath;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;


import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;


@SpringBootTest
public class testIT {

    TestRestTemplate testRestTemplate;
    @Autowired
    LibraryService libraryService;

    String id="";
    @Test
    @Order(1)
    public void getAuthorBookTest() throws JSONException {
        String expected = "[{\"bookName\":\"Devops\",\"id\":\"fdsefr3435\",\"isbn\":\"fdsefr34\",\"aisle\":35,\"author\":\"Vishal\"}]";
        testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("http://localhost:8080/getBooks/author?authorname=rajesh", String.class);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals("We don't have any book with this author", responseEntity.getBody().toString());
        //JSONAssert.assertEquals("We don't have any book with this author", responseEntity.getBody(), false);
    }
    @Test
    @Order(2)
    public void addBookIntegrationTest(){
        testRestTemplate = new TestRestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Library> httpEntity=new HttpEntity<>(buildLibrary(),httpHeaders);
        ResponseEntity<String> responseEntity=testRestTemplate.postForEntity("http://localhost:8080/addbook",httpEntity,String.class);
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getStatusCode());
        System.out.println("OLD"+buildLibrary().getIsbn()+buildLibrary().getAisle());
        System.out.println(responseEntity.getHeaders().get("unique").get(0));
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals("OLD"+buildLibrary().getIsbn()+buildLibrary().getAisle(),responseEntity.getHeaders().get("unique").get(0));
        id=responseEntity.getHeaders().get("unique").get(0);

    }
    @Test
    @Order(3)
    public void addBookIntegrationNegativeTest() throws JSONException {
        String id="OLD"+buildNegativeLibrary().getIsbn()+buildNegativeLibrary().getAisle();
        String expected="{\n" +
                "    \"id\": \""+id+"\",\n" +
                "    \"message\": \"Book already exists\"\n" +
                "}";
        testRestTemplate = new TestRestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Library> httpEntity=new HttpEntity<>(buildNegativeLibrary(),httpHeaders);
        ResponseEntity<String> responseEntity=testRestTemplate.postForEntity("http://localhost:8080/addbook",httpEntity,String.class);
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getStatusCode());
        System.out.println(id);
        Assertions.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        //Assertions.assertEquals(,responseEntity.getHeaders().get("unique").get(0));
        JSONAssert.assertEquals(expected, responseEntity.getBody(), false);
        Assertions.assertTrue(responseEntity.getBody().contains("Book already exists"));
    }
    @Test
    @Order(4)
    public void deleteBookTest(){
        TestRestTemplate restTemplate =new TestRestTemplate();
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);*/
        /*Gson gson = new Gson();
        JsonElement element=gson.fromJson ("{ \"id\" :\"fdsefr343\"}", JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();*/
        HttpEntity<Library> request = new HttpEntity<Library>(deleteLibrary());

        ResponseEntity<String> responseEntity=restTemplate.exchange("http://localhost:8080/deletebook",HttpMethod.DELETE,request,String.class);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(responseEntity.getStatusCode(),HttpStatus.CREATED,"Pass validation match status");

    }

    @Test
    public void updateBookTest(){
        TestRestTemplate restTemplate =new TestRestTemplate();
        HttpEntity<Library> request = new HttpEntity<Library>(updateLibrary());
        ResponseEntity<String> responseEntity=restTemplate.exchange("http://localhost:8080/updatebook/OLDZvjkl34311",HttpMethod.PUT,request,String.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),"Validation result ");
        JsonPath js=new JsonPath(responseEntity.getBody());
        String id=js.getString("author");
        System.out.println(id);

    }

    @Test
     public void getBookDetailsTest(){
        TestRestTemplate restTemplate =new TestRestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/getbooks/fdsefr3435", String.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),"Validation result ");
        JsonPath js=new JsonPath(responseEntity.getBody());
        String id=js.getString("bookName");
        System.out.println(id);
    }
    @Test
    public void getGreetingTest(){
        TestRestTemplate restTemplate =new TestRestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/greeting?name=sahil", String.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),"Validation result ");
        JsonPath js=new JsonPath(responseEntity.getBody());
        String id=js.getString("content");
        Assertions.assertTrue(id.contains("sahil"));
        System.out.println(id);
    }

    int min = 999;
    int max = 9999;
    int b = (int)(Math.random()*(max-min+1)+min);

    public Library updateLibrary(){
        Library library = new Library();

        library.setAisle(12345);
        library.setAuthor("yukti");
        library.setBookName("Yukti Bio");
        return library;
    }

    public Library deleteLibrary() {
        Library library = new Library();

        library.setId("fdse343");
        return library;
    }

    public Library buildLibrary() {
        Library library = new Library();
        library.setBookName("Vishal Ji");
        library.setAisle(b);
        library.setIsbn("Zvjkl");
        library.setAuthor("vishu");
        return library;

    }

    public Library buildNegativeLibrary() {
        Library library = new Library();
        library.setBookName("Vishal Ji");
        library.setAisle(34311);
        library.setIsbn("Zvjkl");
        library.setAuthor("vishu");
        return library;

    }
}
