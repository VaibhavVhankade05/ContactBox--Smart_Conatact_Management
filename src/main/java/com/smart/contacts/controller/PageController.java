package com.smart.contacts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController 
{
	@RequestMapping("/home")
	public String homePage()
	{
		return "home";
	}

}
