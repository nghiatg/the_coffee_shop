package com.cofeeshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cofeeshop.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByShopId(Long shopId);
    
}
