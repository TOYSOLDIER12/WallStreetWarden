package ma.xproce.getrich.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.xproce.getrich.service.EnterpriseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnterpriseController {
    @Autowired
    EnterpriseManager enterpriseManager;
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")){
            System.out.println("logged "+ session.getAttribute("username"));
            return "index.html";
        } else {
            return "redirect:/login";
        }
    }



}
