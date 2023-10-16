package com.cofeeshop.controller.privatecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.ContactDTO;
import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.ContactService;

@RequestMapping("/contact")
@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PutMapping("/{contactId}")
    public GenericApiResponse updateContactInfo(@PathVariable Long contactId, @RequestBody ContactDTO contactDTO) {
        contactService.updateContact(contactId, contactDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
    
}