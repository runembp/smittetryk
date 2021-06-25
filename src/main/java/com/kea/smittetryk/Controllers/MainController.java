package com.kea.smittetryk.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main Controller - Redirects requests to either of the two mappings / and /municipals and has no other responsibility
 * to uphold separation of concerns.
 */
@Controller
public class MainController
{
    @GetMapping("/")
    public String getIndex()
    {
        return "parishes";
    }

    @GetMapping("/municipals")
    public String getMunicipals()
    {
        return "municipals";
    }
}
