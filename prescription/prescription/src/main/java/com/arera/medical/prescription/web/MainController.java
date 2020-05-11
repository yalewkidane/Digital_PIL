package com.arera.medical.prescription.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.arera.medical.prescription.database.PrescriptionDBService;

@Controller
//@RestController
public class MainController {

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    
    @GetMapping("/management")
    public String management() {
        return "management";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/manage_user")
    public String manage_user() {
        return "manage_user";
    }
    
    @GetMapping("/manage_prescription")
    public String manage_prescription() {
        return "manage_prescription";
    }
    
    @GetMapping("/add_prescription")
    public String add_prescription() {
        return "add_prescription";
    }
    //prescription?drugCode=4545
    @GetMapping("/prescription")
    public ModelAndView prescription(@RequestParam(required = false) String drugCode) {
    	ModelAndView modelAndView =new ModelAndView();
    	modelAndView.addObject("qr_code_image","");
    	modelAndView.setViewName("prescription");
    	System.out.println(drugCode);
        return modelAndView;
    }
    
    
  //prescription?drugCode=4545
    @GetMapping("/prescription_get")
    @ResponseBody
    public String prescription_get(@RequestParam(required = false) String drugCode,
    		@RequestParam(required = false) String language) {
    	//System.out.println(drugCode);
    	//System.out.println(language);
    	if(drugCode != null) {
			if(language != null) {
				return PrescriptionDBService.searchPrescription(drugCode, language);
			}
		}else {
			return "";
		}
		return "";
    }
    
}
