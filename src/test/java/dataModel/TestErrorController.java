package dataModel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import Model.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TestErrorController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository repository;

    @Test
    public void requestMalformatedBookIdReturns400() throws Exception {
        String badBookId = "abc";

        mvc.perform(MockMvcRequestBuilders
                .get("/public/viewbook?bookID=" + badBookId)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(view().name("error"))
//                .andExpect(content().string(containsString(ApplicationMsg.BAD_REQUEST.getMsg())))
                .andReturn();
    }

    @Test
    public void requestNonExistentPageReturns404() throws Exception {
        String pageRequest = "/invalidPage";

        mvc.perform(MockMvcRequestBuilders
                .get(pageRequest)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(view().name("error"))
//                .andExpect(content().string(containsString(String.format(ApplicationMsg.INVALID_PAGE.getMsg(), pageRequest))))
                .andReturn();
    }


}