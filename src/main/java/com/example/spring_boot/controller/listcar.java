package com.example.spring_boot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.repository.UserDetailRepository;

import jakarta.servlet.http.HttpSession;

import com.example.spring_boot.entity.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class listcar {

    @GetMapping("/car/download-proof/{cid}")
    public ResponseEntity<byte[]> downloadProof(@PathVariable int cid) {
        byte[] proofDocument = CarsDAO.getProofDocumentByCarId(cid);

        if (proofDocument == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the document with headers to force download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proof_" + cid + ".jpeg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(proofDocument);
    }

    @GetMapping("/listcar")
    public String infopage(Model model,HttpSession session){
        Long id=(Long)session.getAttribute("id");
        List<Cars> m1=CarsDAO.getAllCarsByCustomerId(id);
        model.addAttribute("cars",m1);
        return "listcar";
    }
    @GetMapping("/car/delete")
    public String deletecar(Model model,HttpSession session,@RequestParam Long carId){
        
        return "redirect:/listcar";
    }
}
