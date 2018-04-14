package hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.iryelp.IRYelpMain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YelpController {
    private IRYelpMain yelp;

    public YelpController() throws IOException {
        yelp = new IRYelpMain();
    }

    @RequestMapping("/")
    public ArrayList<HashMap<String, Object>> search(@RequestParam(value = "query", defaultValue = "nice") String query) throws IOException {
        return yelp.search(query);
    }
}
