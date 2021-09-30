package com.gsas.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gsas.model.AddressVO;
import com.gsas.model.CitizenDetailsVO;
import com.gsas.model.IncomeGroupVO;
import com.gsas.model.LoginVO;
import com.gsas.model.ProfessionVO;
import com.gsas.service.CitizenService;
import com.gsas.utility.LayerType;
import com.gsas.utility.ObjectFactory;

/**
 * Servlet implementation class UpdateCitizenDetailsServlet
 */
@WebServlet("/UpdateCitizenDetailsServlet")
public class UpdateCitizenDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public UpdateCitizenDetailsServlet() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CitizenService citizenService = (CitizenService) ObjectFactory.getInstance(LayerType.CITIZEN_SERVICE);
		HttpSession session = request.getSession();
		RequestDispatcher requestDispatcher = null;

		LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
		if(loginVO != null) {	
			loginVO.setUserName(request.getParameter("username"));
			loginVO.setPassword(request.getParameter("password"));
			
			AddressVO addressVO = new AddressVO();
			addressVO.setAddressId(Long.parseLong(request.getParameter("addressId")));
			addressVO.setStreet(request.getParameter("street"));
			addressVO.setCity(request.getParameter("city"));
			addressVO.setState(request.getParameter("state"));
			addressVO.setPincode(Integer.parseInt(request.getParameter("pincode")));
			
			CitizenDetailsVO citizenDetailsVO = new CitizenDetailsVO();
			citizenDetailsVO.setCitizenDetailsId(Long.parseLong(request.getParameter("citizenDetailsId")));
			citizenDetailsVO.setFirstName(request.getParameter("firstName"));
			citizenDetailsVO.setMiddleName(request.getParameter("middleName"));
			citizenDetailsVO.setLastName(request.getParameter("lastName"));
		
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter = formatter.withLocale( Locale.ENGLISH );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
			LocalDate date = LocalDate.parse(request.getParameter("dateOfBirth"), formatter);
			citizenDetailsVO.setDateOfBirth(date);
			
			citizenDetailsVO.setGender(request.getParameter("gender"));
			citizenDetailsVO.setEmail(request.getParameter("email"));
			citizenDetailsVO.setPhone(Long.parseLong(request.getParameter("phone")));
			citizenDetailsVO.setAddressVO(addressVO);
			citizenDetailsVO.setIncomeGroup(new IncomeGroupVO(Long.parseLong(request.getParameter("incomeGroup"))));
			citizenDetailsVO.setProfession(new ProfessionVO(Long.parseLong(request.getParameter("profession"))));
			citizenDetailsVO.setAdharNumber(Long.parseLong( request.getParameter("adharNumber") ));
			citizenDetailsVO.setPancardNumber(request.getParameter("pancardNumber"));
			citizenDetailsVO.setLoginVO(loginVO);
			citizenService.updateCitizenDetails(citizenDetailsVO);
			
			session.setAttribute("loginVO", loginVO);
			request.setAttribute("message", "CitizenDetails Updated Successfully for Citizen Name: "+loginVO.getUserName());
			
			
			requestDispatcher = request.getRequestDispatcher("viewSchemesCitizenServlet");
			requestDispatcher.forward(request, response);
		}
		else {
			requestDispatcher = request.getRequestDispatcher("citizenLogin.jsp");
			request.setAttribute("err","Sorry, You are not Authorized to view this Page");
			requestDispatcher.forward(request, response);
		}
	}

}
