package com.cofeeshop.service.impl;

import static com.cofeeshop.config.Constants.NOT_ENOUGH_PERMISSION;
import static com.cofeeshop.config.Constants.SHOP_CHAIN_ALREADY_DELETED;
import static com.cofeeshop.config.Constants.SHOP_CHAIN_NOT_FOUND;
import static com.cofeeshop.config.Constants.SHOP_NOT_BELONG_TO_SHOP_CHAIN;
import static com.cofeeshop.config.Constants.SHOP_NOT_FOUND;
import static com.cofeeshop.entity.Status.AVAILABLE;
import static com.cofeeshop.entity.Status.DELETED;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cofeeshop.dto.ShopChainDTO;
import com.cofeeshop.dto.ShopDTO;
import com.cofeeshop.entity.Shop;
import com.cofeeshop.entity.ShopChain;
import com.cofeeshop.exception.ClientException;
import com.cofeeshop.repository.ShopChainRepository;
import com.cofeeshop.repository.ShopRepository;
import com.cofeeshop.service.ShopChainService;
import com.cofeeshop.service.ShopService;
import com.cofeeshop.util.UserUtils;

@Service
public class ShopChainServiceImpl implements ShopChainService {

    @Autowired
    private ShopChainRepository shopChainRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserUtils userUtils;

    @Override
    public List<ShopChainDTO> retrieveShopChainList() {
        String currentUserName = userUtils.retrieveCurrentUser();
        List<ShopChain> shopChainEntities = shopChainRepository.findByShopChainOwner(currentUserName);
        Function<ShopChainDTO, ShopChainDTO> addShopsInfoIntoDTO = dto -> dto.toBuilder().shops(retrieveShopDTOFromShopChainId(dto.getId())).build();
        return shopChainEntities.stream()
                .map(entity -> new ShopChainDTO(entity))
                .map(addShopsInfoIntoDTO::apply)
                .collect(Collectors.toList());
    }

    private List<Shop> retrieveShopsFromShopChainId(Long shopChainId) {
        return shopRepository.findByShopChainId(shopChainId);
    }

    private List<ShopDTO> retrieveShopDTOFromShopChainId(Long shopChainId) {
        return retrieveShopsFromShopChainId(shopChainId).stream().map(shopEntity -> new ShopDTO(shopEntity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteShopChain(Long shopChainId) {
        ShopChain deletedShopChain = getShopChainFromId(shopChainId);
        shopRepository.findByShopChainId(shopChainId).forEach(shopService::deleteShop);
        deletedShopChain.setStatus(DELETED);
        shopChainRepository.save(deletedShopChain);
    }

    @Override
    public void updateShopChain(Long shopChainId, ShopChainDTO shopChainDTO) {
        ShopChain updatedShopChain = getShopChainFromId(shopChainId);
        Optional.ofNullable(shopChainDTO.getShopChainName()).ifPresent(updatedShopChain::setShopChainName);
        Optional.ofNullable(shopChainDTO.getShopChainOwner()).ifPresent(updatedShopChain::setShopChainOwner);
        shopChainRepository.save(updatedShopChain);
    }


    private ShopChain getShopChainFromId(Long id) {
        ShopChain retrievedShopChain = shopChainRepository.findById(id).orElseThrow(() -> new ClientException(SHOP_CHAIN_NOT_FOUND));
        String currentUserName = userUtils.retrieveCurrentUser();
        Optional.of(retrievedShopChain.getShopChainOwner())
                .map(ownerName -> ownerName.equals(currentUserName))
                .orElseThrow(() -> new ClientException(NOT_ENOUGH_PERMISSION));
        return retrievedShopChain;
    }

    @Override
    public Long addShopToShopChain(Long shopChainId, ShopDTO shopDTO) {
        ShopChain shopChain = getShopChainFromId(shopChainId);
        Optional.ofNullable(shopChain.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(SHOP_CHAIN_ALREADY_DELETED));
        Shop createdShop = new Shop();
        BeanUtils.copyProperties(shopDTO, createdShop);
        createdShop.setShopChainId(shopChain.getId());
        createdShop.setStatus(AVAILABLE);
        return shopRepository.save(createdShop).getId();
    }

    @Override
    public Long createShopChain(ShopChainDTO shopChainDTO) {
        ShopChain createdShopChain = new ShopChain();
        BeanUtils.copyProperties(shopChainDTO, createdShopChain);
        createdShopChain.setShopChainOwner(userUtils.retrieveCurrentUser());
        createdShopChain.setStatus(AVAILABLE);
        return shopChainRepository.save(createdShopChain).getId();
    }

    @Override
    @Transactional
    public void deleteShop(Long shopChainId, Long shopId) {
        getShopChainFromId(shopChainId);
        Shop removedShop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        Optional.of(removedShop.getShopChainId()).filter(shopChainId::equals).orElseThrow(() -> new ClientException(SHOP_NOT_BELONG_TO_SHOP_CHAIN));
        shopService.deleteShop(removedShop);
    }

    
    
    

    
}
