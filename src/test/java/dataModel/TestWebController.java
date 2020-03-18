package dataModel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    public void viewIndexTestReturnsProperCount() throws Exception {
        long currCount = repository.count();

        mvc.perform(MockMvcRequestBuilders
                .get("/view")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
//                .andExpect(model().attribute("books", repository.findAll())) //Results should be identical
                .andReturn();

        assertEquals(currCount, repository.count());
    }

    @Test
    public void requestValidBookId() throws Exception {
        long requestId = 1; //rework this logic

        Book b = repository.findById(requestId);

        mvc.perform(MockMvcRequestBuilders
                .get("/viewbook?bookID=" + requestId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
//                .andExpect(model().attribute("books", repository.findById(requestId)))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void requestInvalidBookIdRedirectsToMainPage() throws Exception {
        long requestId = 0;

        mvc.perform(MockMvcRequestBuilders
                .get("/viewbook?bookID=" + requestId)
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

        Book newBook = new Book("ABCs of Counting", "R. W. Swan", 2010, "The story takes place in an imagined soup can.", 4.99, 1);

        mvc.perform(MockMvcRequestBuilders
                .post("/addbook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .sessionAttr("newBook", newBook))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andReturn();

        assertEquals(currCount + 1, repository.count());
    }

    @Test
    public void searchByTitleExists() throws Exception {
        long requestId = 1; //rework this logic
        Book b = repository.findById(requestId);

        mvc.perform(MockMvcRequestBuilders
                .post("/searchByTitle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", b.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                //.andExpect(model().attribute("books", repository.findByTitle(b.getTitle())))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void searchByTitleNotFoundModelIsNull() throws Exception {
        String invalidBookTitle = "";

        mvc.perform(MockMvcRequestBuilders
                .post("/searchByTitle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", invalidBookTitle))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attribute("books", repository.findByTitle(invalidBookTitle)))
                .andReturn();
    }

    @Test
    public void searchByAuthorExists() throws Exception {
        long requestId = 1; //rework this logic
        Book b = repository.findById(requestId);

        mvc.perform(MockMvcRequestBuilders
                .post("/searchByAuthor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("author", b.getAuthor()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                //.andExpect(model().attribute("books", repository.findByAuthor(b.getAuthor())))
                .andExpect(content().string(containsString(b.getAuthor())))  //Until can find a better way to test model obj directly
                .andReturn();
    }

    @Test
    public void searchByAuthorNotFoundModelIsNull() throws Exception {
        String invalidAuthorName = "";

        mvc.perform(MockMvcRequestBuilders
                .post("/searchByAuthor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("author", invalidAuthorName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attribute("books", repository.findByAuthor(invalidAuthorName)))
                .andReturn();
    }

}