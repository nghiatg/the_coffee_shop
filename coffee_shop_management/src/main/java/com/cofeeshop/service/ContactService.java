package com.cofeeshop.service;

import com.cofeeshop.dto.ContactDTO;

public interface ContactService {

    void updateContact(Long contactId, ContactDTO contactDTO);
    
}
