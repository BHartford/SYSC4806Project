package dataModel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class TestWebController {

    public static final Book newBook = new Book("ABCs of Counting", "R. W. Swan", 2010, "The story takes place in an imagined soup can.", 4.99, 1);


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
                .andExpect(model().attribute("books", repository.findAll())) //Results should be identical
                .andReturn();

        assertEquals(currCount, repository.count());
    }

    @Test
    public void requestValidBookId() throws Exception {
        long requestId = 1; //rework this logic

        mvc.perform(MockMvcRequestBuilders
                .get("/viewbook?bookID=" + requestId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attribute("books", repository.findById(requestId)))
                .andReturn();
    }

    @Test
    public void requestInvalidBookIdRedirectsToMainPage() throws Exception {
        long requestId = repository.count() + 1000; //rework this logic

        mvc.perform(MockMvcRequestBuilders
                .get("/viewbook?bookID=" + requestId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMsg", requestId + " is an invalid requestNumber"))
                .andReturn();
    }

    @Test
    public void requestInvalidBookIdAlphabeticReturnsErrorPage() throws Exception {
        String requestId = "abc";

        mvc.perform(MockMvcRequestBuilders
                .get("/viewbook?bookID=" + requestId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addValidBookTest() throws Exception {
        long currCount = repository.count();

        mvc.perform(MockMvcRequestBuilders
                .post("/addbook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .sessionAttr("newBook", newBook))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("index"))
                .andReturn();

        assertEquals(currCount + 1, repository.count());
    }

}