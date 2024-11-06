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
import java.util.stream.Collectors;
@Controller
public class buy {
@GetMapping("/buy")
public String buycar(Model model,@RequestParam(required = false) String engineType,
@RequestParam(required = false) Integer mileageMin,
@RequestParam(required = false) Integer mileageMax,
@RequestParam(required = false) Integer priceMin,
@RequestParam(required = false) Integer priceMax,
@RequestParam(required = false) String sortOrder)
{

    
    System.out.println(engineType);
    // for (int i = 0; i < cata.size(); i++) {
    //     catalogue car = cata.get(i);
    //     System.out.println(car.getModel_id());
    // }
    if(mileageMin==null) mileageMin=0;
    if(mileageMax==null) mileageMax=Integer.MAX_VALUE;
    if(priceMin==null) priceMin=0;
    if(priceMax==null) priceMax=Integer.MAX_VALUE;

    if (sortOrder != null) {
            switch (sortOrder) {
                case "priceAsc":
                   sortOrder="price_asc";
                    break;
                case "priceDesc":
                    sortOrder="price_desc";
                    break;
                case "mileageAsc":
                    sortOrder="mileage_asc";
                    break;
                case "mileageDesc":
                    sortOrder="mileage_desc";
                    break;
                default:
                    break;
            }
        }
    List<catalogue> cata=CatalogueRepo.getAllCatalogueEntries(mileageMin,mileageMax,priceMin,priceMax,sortOrder,engineType);
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
