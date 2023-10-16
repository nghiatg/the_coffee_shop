package com.cofeeshop.dto;

import org.springframework.beans.BeanUtils;

import com.cofeeshop.entity.Contact;
import com.cofeeshop.entity.ContactType;
import com.cofeeshop.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
@AllArgsConstructor
public class ContactDTO {
    
    private Long id;

    private ContactType type;

    private String value;

    private ShopDTO shop;
    
    private Status status;

    public ContactDTO(Contact contact) {
        BeanUtils.copyProperties(contact, this);
    }

}
