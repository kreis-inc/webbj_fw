package jp.bj_one.re.webservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class RootController {
	@RequestMapping({"re", "re/"})
	public String index() {
		return "Hello Report Engine!";
	}
}
