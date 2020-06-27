package admin.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping("/login")
    String login()
    {
        return "login";
    }

    @GetMapping("/checkOut")
    String checkOut()
    {
        return "checkOut";
    }

    @GetMapping("/record")
    String record()
    {
        return "record";
    }

    @GetMapping("/billPage")
    String billPage()
    {
        return "billPage";
    }
}
