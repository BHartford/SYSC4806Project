package Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import Model.ApplicationMsg;
import Model.Book;
import Model.BookRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestWebController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository repository;

    @Test
    public void viewIndexTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/view")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
//                .andExpect(model().attribute("books", repository.findAll())) //Results should be identical
                .andReturn();
    }

    @Test
    public void requestValidBookId() throws Exception {
        Iterable<Book> books = repository.findAll();
        Book b = books.iterator().next();

        mvc.perform(MockMvcRequestBuilders
                .get("/public/viewbook?bookID=" + b.getId())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
//                .andExpect(model().attribute("books", repository.findById(requestId)))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void requestBookIdNotInDatabaseRedirectsToMainPage() throws Exception {
        long requestId = 0;

        mvc.perform(MockMvcRequestBuilders
                .get("/public/viewbook?bookID=" + requestId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMsg", String.format(ApplicationMsg.BAD_BOOK_ID.getMsg(), requestId)))
                .andReturn();
    }

//    @Test
//    public void requestInvalidBookIdAlphabeticReturnsErrorPage() throws Exception {
//        String requestId = "abc";
//
//        mvc.perform(MockMvcRequestBuilders
//                .get("/viewbook?bookID=" + requestId)
//                .contentType(MediaType.TEXT_HTML))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andReturn();
//    }

    @Test
    public void addValidBookTest() throws Exception {
        long currCount = repository.count();

        Book newBook = new Book("ABCs of Counting", "R. W. Swan", 2010, "The story takes place in an imagined soup can.", 4.99, 1, 2.0);

        mvc.perform(MockMvcRequestBuilders
                .post("/private/addbook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .sessionAttr("newBook", newBook))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andReturn();

        assertEquals(currCount + 1, repository.count());
    }

    @Test
    public void searchByTitleExists() throws Exception {
        Iterable<Book> books = repository.findAll();
        Book b = books.iterator().next();

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByTitle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", b.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                //.andExpect(model().attribute("books", repository.findByTitle(b.getTitle())))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void searchByTitleIsNullReturnsIndex() throws Exception {
        String nonexistentBookTitle = "";

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByTitle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", nonexistentBookTitle))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString(String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), nonexistentBookTitle))))
                .andReturn();
    }

    @Test
    public void searchByTitleDoesNotExistReturnsIndex() throws Exception {
        String nonexistentBookTitle = "Pan De Mic";

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByTitle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", nonexistentBookTitle))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString(String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), nonexistentBookTitle))))
                .andReturn();
    }

    @Test
    public void searchByAuthorExists() throws Exception {
        Iterable<Book> books = repository.findAll();
        Book b = books.iterator().next();

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByAuthor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("author", b.getAuthor()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                //.andExpect(model().attribute("books", repository.findByAuthor(b.getAuthor())))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void searchByAuthorIsEmptyReturnsIndex() throws Exception {
        String nonexistentAuthorName = "";

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByAuthor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("author", nonexistentAuthorName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString(String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), nonexistentAuthorName))))
                .andReturn();
    }

    @Test
    public void searchByAuthorDoesNotExistReturnsIndex() throws Exception {
        String nonexistentAuthorName = "C.O. Rona";

        mvc.perform(MockMvcRequestBuilders
                .post("/public/searchByAuthor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("author", nonexistentAuthorName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString(String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), nonexistentAuthorName))))
                .andReturn();
    }

}