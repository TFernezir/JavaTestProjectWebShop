package example.WebShopTrening;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<Object> handelSQLIntegrityException(HttpServletRequest req, SQLIntegrityConstraintViolationException ex){
		
		String error = "Unable to submit post: " + ex.getMessage();
		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleNoSuchElementException(HttpServletRequest req, NoSuchElementException ex) {
	   return buildResponseEntity(new ErrorResponse(
	       HttpStatus.NOT_FOUND,
	       String.format("Resource not found at path: %s", req.getRequestURI())
	   ));
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
		String message = Optional.ofNullable(ex.getRequiredType())
	            .map(Class::getName)
	            .map(className -> String.format("[%s] should be of type [%s]", ex.getName(), className))
	            .orElseGet(ex::getMessage);
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, message);
            
        return buildResponseEntity(response);
    }
	
	@ExceptionHandler(IncorrectUpdateSemanticsDataAccessException.class)
    public ResponseEntity<Object> handleIncorrectUpdate(HttpServletRequest req, IncorrectUpdateSemanticsDataAccessException ex) {
		String message = ex.getMessage();
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, message);
            
        return buildResponseEntity(response);
    }
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	    MethodArgumentNotValidException ex, 
	    HttpHeaders headers, 
	    HttpStatusCode status, 
	    WebRequest request) {
	        
		String error = ex.getBindingResult().getFieldErrors().stream()
			       .map(fieldError -> String.format("Field [%s] %s", 
			           fieldError.getField().toUpperCase(),
			           fieldError.getDefaultMessage()))
			       .findFirst()
			       .orElse("Validation failed");
	        
	    return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }	
	
	
	private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse)
	{
		return new ResponseEntity<Object>(errorResponse, errorResponse.status());
	}
}
