package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.spring_boot.repository.CatalogueRepo;
import com.example.spring_boot.entity.catalogue;
import org.springframework.ui.Model;
import java.util.List;
@Controller
public class buy {
@GetMapping("/buy")
public String buycar(Model model)
{
    List<catalogue> cata=CatalogueRepo.getAllCatalogueEntries();
    // for (int i = 0; i < cata.size(); i++) {
    //     catalogue car = cata.get(i);
    //     System.out.println(car.getModel_id());
    // }
    model.addAttribute("cata", cata);
    return "buy";
}
}
