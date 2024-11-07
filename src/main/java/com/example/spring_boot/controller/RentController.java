package com.example.spring_boot.controller;

import com.example.spring_boot.repository.CatalogueRepo;
import com.example.spring_boot.repository.RentCarDAO;
import com.example.spring_boot.repository.RentRepository;
import com.example.spring_boot.repository.RenterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import com.example.spring_boot.entity.*;
@Controller
public class RentController {

    @GetMapping("/rent")
    public String showRenterDetailsForm(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        boolean isExistingRenter = RenterRepository.isPresentById(id);

        if (!isExistingRenter) {
            Renter renter = new Renter();
            renter.setRenter_id(id);
            model.addAttribute("rental", renter);
            return "RenterDetails";
        }
        if(id==null) return "/info";
       
        Renter rnr=RenterRepository.findById(id);
        if(rnr.getRenter_id()==null){
            return "Sorry";
        }
        model.addAttribute("renterdetails", rnr);
        return "rent_info";
    }

    @PostMapping("/rentdetails")
    public String saveRenterDetails(
            @Valid @ModelAttribute("rental") Renter rental,
            BindingResult result,
            Model model,
            HttpSession session) {

        if (result.hasErrors()) {
            return "RenterDetails";
        }
        Long id=(Long)session.getAttribute("id");
        rental.setRenter_id(id);
        rental.setStatus("Pending");  // Default status
        rental.setSignup_date(new Date());  // Default signup date
        int saveresult=RenterRepository.save(rental);
        if(saveresult==0){
            return "RenterDetails";
        }
        // Save renter in the database
        model.addAttribute("rental",rental);
        session.setAttribute("id", id);
        return "rent_form";
    }
    @PostMapping("/rent_form")
    public String saveRenterForm(@Valid @ModelAttribute("rental") Renter rental,BindingResult result,Model model,HttpSession session){
        if (result.hasErrors()) {
            return "RenterDetails";
        }
        Long id=(Long)session.getAttribute("id");
        boolean isExistingRenter = RenterRepository.isPresentById(id);

        if (!isExistingRenter) {
            Renter renter = new Renter();
            renter.setRenter_id(id);
            model.addAttribute("rental", renter);
            return "RenterDetails";
        }
        
        System.out.println("id for renter:"+id);
        RenterRepository.updateField(id,"form_path",rental.getForm_path());
        return "redirect:/rent";
        
    }
   
    @GetMapping("/rent-car")
    public String rentCar(Model model,
                          @RequestParam(required = false) String engineType,
                          @RequestParam(required = false) Integer mileageMin,
                          @RequestParam(required = false) Integer mileageMax,
                          @RequestParam(required = false) Integer priceMin,
                          @RequestParam(required = false) Integer priceMax,
                          @RequestParam(required = false) String sortOrder,HttpSession s,RedirectAttributes r) {

                            boolean check=RenterRepository.isActiveById((Long)s.getAttribute("id"));

                            if(!check) {
                                r.addFlashAttribute("msg","Not Verified");
                                return "redirect:/rent";}

        if (mileageMin == null) mileageMin = 0;
        if (mileageMax == null) mileageMax = Integer.MAX_VALUE;
        if (priceMin == null) priceMin = 0;
        if (priceMax == null) priceMax = Integer.MAX_VALUE;

        // Convert sort order for rent sorting logic
        if (sortOrder != null) {
            switch (sortOrder) {
                case "priceAsc":
                    sortOrder = "price_asc";
                    break;
                case "priceDesc":
                    sortOrder = "price_desc";
                    break;
                case "mileageAsc":
                    sortOrder = "mileage_asc";
                    break;
                case "mileageDesc":
                    sortOrder = "mileage_desc";
                    break;
            }
        }

        List<catalogue> cata = CatalogueRepo.getAllCatalogueEntries(mileageMin, mileageMax, priceMin, priceMax, sortOrder, engineType);
        model.addAttribute("cata", cata);
        return "rent_car";
    }

    @PostMapping("/rent-availability")
    public String checkRentAvailability(Model model, @RequestParam("car_id") Long modelId, HttpSession session,RedirectAttributes r) {
        boolean available = RentRepository.isModelAvailableForRent(modelId);
        if (!available) {
            return "Sorry"; // Display a message or page indicating unavailability
        }
        r.addFlashAttribute("msg","Request Sent");
        RentCarDAO.addNewRent( (Long) session.getAttribute("id"),modelId);
        return "redirect:/rent-car"; // Display a success message or page confirming the request
    }

    @GetMapping("/track-rentals")
    public String trackRentalOrders(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        List<Rent> inProgressRentals = RentCarDAO.getPendingRentsByRenterId(id);
        List<Rent> rejectedRentals = RentCarDAO.getRejectedRentsByRenterId(id);
        model.addAttribute("inProgressRentals", inProgressRentals);
        model.addAttribute("rejectedRentals", rejectedRentals);
        return "track-rentals";
    }

    // @PostMapping("/rent-model-det")
    // public String rentModelDetails(Model model, @RequestParam Long modelId) {
    //     catalogue c = CatalogueRepo.getCatalogueById(modelId);
    //     model.addAttribute("c", c);
    //     return "rent-model-det";
    // }

     @GetMapping("/rent/track")
    public String renttrack(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        List<Rent> r=RentCarDAO.getActiveRentsByRenter(id);
        model.addAttribute("r", r);
        return "rent-customer";
    }
}
