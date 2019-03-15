package com.kevin.searchengine;

        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.ResponseBody;

        import javax.servlet.http.HttpServletRequest;
        import java.util.List;
        import java.util.Map;

@Controller
public class SearchController {



    @RequestMapping("/query")
    @ResponseBody
    public List<Map> main(Model model,@RequestParam String keyword) {

        List<Map> qr = EsMain.query(keyword);

        return qr;
    }

    @RequestMapping("/makeIndex")
    @ResponseBody
    public Object makeIndex(HttpServletRequest request,Model model, @RequestParam String words) {

        int qr = EsMain.makeIndex(words.split(","),request);

        return qr;
    }


}
