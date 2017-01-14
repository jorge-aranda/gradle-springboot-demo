
package es.jaranda.poc.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
public class GradleSpringbootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradleSpringbootDemoApplication.class, args);
	}

}

@XmlRootElement(name = "response")
class ResponseDto {

	private boolean success;

    public ResponseDto() {

    }

    public ResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

@RestController
class MainController {

    @RequestMapping(value = "main", method = GET)
	public ResponseDto root() {
	    return new ResponseDto(true);
    }

}
