package hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.iryelp.AlternativeMain;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class YelpController {
    private AlternativeMain yelp;

    public YelpController() throws IOException {
        yelp = new AlternativeMain();
    }

    @RequestMapping("/")
    public ArrayList<HashMap<String, Object>> search(@RequestParam(value = "query", defaultValue = "nice") String query) throws IOException {
        return yelp.search(query);
    }
}
