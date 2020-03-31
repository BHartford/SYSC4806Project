package dataModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import Logging.LoggingLibrary;

@Controller
public class BookStoreErrorController implements ErrorController {
	@Value("${kafka.logging}")
	private boolean kafkaLogging;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	private static final String ERROR_TOPIC = "error";

	@RequestMapping("/error")
	public String handleError(Model model, HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			String errorMsg;

			if (statusCode == HttpStatus.BAD_REQUEST.value()) { // 400
				errorMsg = ApplicationMsg.BAD_REQUEST.getMsg();

			} else if (statusCode == HttpStatus.UNAUTHORIZED.value()) { // 401
				errorMsg = ApplicationMsg.USER_NOT_AUTH.getMsg();

			} else if (statusCode == HttpStatus.FORBIDDEN.value()) { // 403
				errorMsg = ApplicationMsg.REQUEST_REFUSED.getMsg();

			} else if (statusCode == HttpStatus.NOT_FOUND.value()) { // 404
				String reqURI = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
				errorMsg = String.format(ApplicationMsg.INVALID_PAGE.getMsg(), reqURI);

			} else { // Other error
				errorMsg = ApplicationMsg.GENERIC_ERROR.getMsg();
			}
			
			if (kafkaLogging) kafkaTemplate.send(ERROR_TOPIC, LoggingLibrary.errorLog(statusCode, errorMsg));

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
