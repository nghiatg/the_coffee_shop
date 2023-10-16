package com.cofeeshop.service.impl;

import static com.cofeeshop.config.Constants.CONTACT_NOT_FOUND;
import static com.cofeeshop.config.Constants.SHOP_NOT_FOUND;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshop.dto.ContactDTO;
import com.cofeeshop.entity.Contact;
import com.cofeeshop.entity.Shop;
import com.cofeeshop.exception.ClientException;
import com.cofeeshop.repository.ContactRepository;
import com.cofeeshop.repository.ShopRepository;
import com.cofeeshop.service.ContactService;
import com.cofeeshop.service.ShopService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Override
    public void updateContact(Long contactId, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new ClientException(CONTACT_NOT_FOUND));
        Shop shop = shopRepository.findById(contact.getShopId()).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        shopService.checkCurrentUserCanManageShop(shop);

        Optional.ofNullable(contactDTO.getType()).ifPresent(contact::setType);
        Optional.ofNullable(contactDTO.getValue()).ifPresent(contact::setValue);

        contactRepository.save(contact);   
    }
    
}
