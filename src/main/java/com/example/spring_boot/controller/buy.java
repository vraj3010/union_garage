package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.repository.CatalogueRepo;
import com.example.spring_boot.repository.SellCarDAO;

import jakarta.servlet.http.HttpSession;

import com.example.spring_boot.entity.Cars;
import com.example.spring_boot.entity.SellBuffer;
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

@PostMapping("/avail")
public String check(Model model,@RequestParam("car_id") Long model_id,HttpSession s){
    System.out.println(model_id);
    boolean avail=SellCarDAO.isSellQuantityAvailable(model_id);
    if(!avail) 
    return "Sorry";
    SellCarDAO.addBuyRequest(model_id, (Long)s.getAttribute("id"));
    return "Request_sent";
}

@GetMapping("track-order")
public String track(Model model,HttpSession s){
    Long id=(Long)s.getAttribute("id");
    List<SellBuffer> s1=SellCarDAO.getInProgressEntriesByCustId(id);
    List<SellBuffer> s2=SellCarDAO.getRejectedEntriesByCustId(id);
    model.addAttribute("s2", s2);
    model.addAttribute("s1",s1);
    return "track-order";
}
@PostMapping("/model-det")
public String modeldetails(Model model,@RequestParam Long modelId) {
    catalogue c=CatalogueRepo.getCatalogueById(modelId);
    model.addAttribute("c", c);
    return "model-det";
}

}
