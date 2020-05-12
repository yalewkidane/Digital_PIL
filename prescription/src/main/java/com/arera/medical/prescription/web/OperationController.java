package com.arera.medical.prescription.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.arera.medical.prescription.database.PrescriptionDBService;
import com.arera.medical.prescription.database.UserDBService;
import com.arera.medical.prescription.qrCodeGenerator.QrCodeGeneratorDemo;


@RestController
public class OperationController {
	
	@RequestMapping(value="/getTest", method=RequestMethod.GET)
	@ResponseBody
	public String test() {
		
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		
		return "Test";
	}
	
	@RequestMapping(value="/searchEmails", method=RequestMethod.GET)
	@ResponseBody
	public String searchEmails(@RequestParam(required = false) String status) {
		
		if(status != null) {
			
			if(status.equals("approved")) {
				String result = UserDBService.serchBySatus("approved");
				return result;
				
			}else if(status.equals("unapproved")) {
				String result = UserDBService.serchBySatus("unapproved");
				return result;
				
			}else if(status.equals("admin")) {
				String result = UserDBService.serchByRole("ADMIN");
				return result;
			}
		}
		
		return "";
	}
	
	@RequestMapping(value="/updateWithEmail", method=RequestMethod.GET)
	@ResponseBody
	public String updateWithEmail(@RequestParam(required = false) String email, 
			@RequestParam(required = false) String operation) {
		
		if(email != null) {
			if(operation != null) {
				
				
				if(operation.equals("search")) {
					String result = UserDBService.searchByEmail(email);
					return result;
					
				}else if(operation.equals("approve")) {
					String result = UserDBService.updateStatus(email, "approved");
					if(result != null) {
						return email+ " is approved!";
					}else {
						return "error";
					}
					
				}else if(operation.equals("admin")) {
					String result = UserDBService.updateRole(email,"ADMIN");
					if (result != null) {
						return "User "+ email+" is upgraded to Admin"; 
					}else {
						return "error";
					}
					
					
				}else if(operation.equals("delete")) {
					String result = UserDBService.deleteByEmail(email);
					if (result != null) {
						return "User "+ email+" is deleted"; 
					}else {
						return "error";
					}
					
				}
			}
			
		}
		
		return "";
	}
	
	
	
	
	@RequestMapping(value="/generateQrCode", method=RequestMethod.POST)
	@ResponseBody
	public String generateQrCode(@RequestBody String body, HttpServletRequest request) {
		
		System.out.println(body);
		
		
		Document doc = Document.parse(body);
		String drug_code = doc.getString("code");
		String laguage=doc.getString("language");
		
		System.out.println(doc.getString("code"));
		
		long conunt_ = PrescriptionDBService.count_prescription(drug_code, laguage);
		if(conunt_ >0) {
			PrescriptionDBService.update_prescription(drug_code, laguage, doc);
			return "The Prescription is updated";
		}else {
			long count = PrescriptionDBService.count_prescription(drug_code);
			if(count>0) {
				PrescriptionDBService.save_prescription(doc);
				return "Additional Prescription is added to drug "+ drug_code;
			}else {
				String url = request.getRequestURL().toString();
				url = url.replaceAll("generateQrCode", "");
				String full_url=url+"prescription?drugCode="+drug_code;
				try {
					QrCodeGeneratorDemo.low_txt_gr_gen(full_url, drug_code);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PrescriptionDBService.save_prescription(doc);
				return "Qr code is generatated and  Prescription is added to the database ";
				
			}
		}
	}
	
	
	
	@RequestMapping(value="/searchPrescription", method=RequestMethod.GET)
	@ResponseBody
	public String searchPrescription(@RequestParam(required = false) String code, 
			@RequestParam(required = false) String language) {
		
		if(code != null) {
			if(language != null) {
				return PrescriptionDBService.searchPrescription(code, language);
			}
		}else {
			return "";
		}
		return "";
	}
	
	
	@RequestMapping(value="/searchPrescriptionLanguage", method=RequestMethod.GET)
	@ResponseBody
	public String searchPrescriptionLanguage(@RequestParam(required = false) String language) {
		//System.out.println(language);
		if(language != null) {
			String result = PrescriptionDBService.searchPrescriptionByLanguage(language);
			//System.out.println(result);
			return result;
		}else {
			return "{}";
		}
	}
	
	@RequestMapping(value="/updatePrescription", method=RequestMethod.POST)
	@ResponseBody
	public String updatePrescription(@RequestBody String body, HttpServletRequest request) {
		
		System.out.println(body);
		
		
		Document doc = Document.parse(body);
		String drug_code = doc.getString("code");
		String laguage=doc.getString("language");
		
		long conunt_ = PrescriptionDBService.count_prescription(drug_code, laguage);
		if(conunt_ >0) {
			PrescriptionDBService.update_prescription(drug_code, laguage, doc);
			return "The Prescription is updated";
		}else {
			return "Prescription doesnt exist";
		}
		
	}
	
	@RequestMapping(value="/deletePrescription", method=RequestMethod.GET)
	@ResponseBody
	public String deletePrescription(@RequestParam(required = false) String code, 
			@RequestParam(required = false) String language) {
		
		if(code != null) {
			if(language != null) {
				String result = PrescriptionDBService.deletePrescription(code, language);
				if (result!= null) {
					String imagesPath_str ="src/main/resources/static/QR_Images/"+code+".png";
					Path imagesPath = Paths.get(imagesPath_str);
					try {
						Files.delete(imagesPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "Prescription deleted";
				}else {
					return "error";
				}
			}
		}else {
			return "error";
		}
		return "";
	}

}
