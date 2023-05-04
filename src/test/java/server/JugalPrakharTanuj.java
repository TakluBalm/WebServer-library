package server;

import javax.swing.text.html.HTML;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Controller(URL = "/tanuj/{id}")
public class JugalPrakharTanuj {

	@MethodHandler(method = "GET")
	Response handler(Request r, String id) throws IOException {
		return new HTMLResponse("1.1", "src/test/resources/test_1.html").setCookie("set-cookie", "123");
	}
}