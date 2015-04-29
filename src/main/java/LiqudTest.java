import com.goldv.io.DirectoryBasedTemplateCache;
import com.goldv.template.Template;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by vince on 22/04/15.
 */
public class LiqudTest {

    public static void main(String... args) throws Exception{


//        Template tmpl = new Template(ClassLoader.getSystemResourceAsStream("test.liquid"));
        List<Integer> items = new LinkedList<>();
        items.add(1);
        items.add(31);
        items.add(32);

        Map<String, Object> scope = new HashMap<>();
        scope.put("item", "blah blah");
        scope.put("array", items);

        StringWriter sw = new StringWriter();

        DirectoryBasedTemplateCache loader = new DirectoryBasedTemplateCache("src/main/resources");
        loader.intitialize();

        loader.forName("test").render(scope,sw);
        //loader.forName("partial/header").render(scope,sw);

        System.out.println(sw.toString());
    }
}
