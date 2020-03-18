package dataModel;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class BookStoreErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            String errorMsg;

            if (statusCode == HttpStatus.BAD_REQUEST.value()) { //400
                //TODO Log User gave an unexpected input
                errorMsg = ApplicationMsg.BAD_REQUEST.getMsg();

            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) { //401
                //TODO Log User tried to preform an unauthorized request
                errorMsg = ApplicationMsg.USER_NOT_AUTH.getMsg();

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) { //403
                //TODO Log User tried to preform an unauthorized request
                errorMsg = ApplicationMsg.REQUEST_REFUSED.getMsg();

            } else if (statusCode == HttpStatus.NOT_FOUND.value()) { //404
                //TODO Log user tried to access an invalid URL
                String reqURI = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();

                errorMsg = String.format(ApplicationMsg.INVALID_PAGE.getMsg(), reqURI);

            } else { //Other error
                //TODO Log additional request information
                errorMsg = ApplicationMsg.GENERIC_ERROR.getMsg();
            }

            model.addAttribute("errorStatus", statusCode);
            model.addAttribute("errorMsg", errorMsg);
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
