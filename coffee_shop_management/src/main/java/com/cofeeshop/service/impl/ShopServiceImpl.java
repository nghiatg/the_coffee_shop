package com.cofeeshop.service.impl;

import static com.cofeeshop.config.Constants.CONTACT_NOT_FOUND;
import static com.cofeeshop.config.Constants.DISH_NOT_FOUND;
import static com.cofeeshop.config.Constants.NOT_ENOUGH_PERMISSION;
import static com.cofeeshop.config.Constants.ORDER_QUEUE_NOT_FOUND;
import static com.cofeeshop.config.Constants.SHOP_ALREADY_DELETED;
import static com.cofeeshop.config.Constants.SHOP_CHAIN_NOT_FOUND;
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

import com.cofeeshop.config.Constants;
import com.cofeeshop.dto.ContactDTO;
import com.cofeeshop.dto.DishDTO;
import com.cofeeshop.dto.ReservedOrderQueueDTO;
import com.cofeeshop.dto.ShopChainDTO;
import com.cofeeshop.dto.ShopDTO;
import com.cofeeshop.entity.Contact;
import com.cofeeshop.entity.Dish;
import com.cofeeshop.entity.ReservedOrderQueue;
import com.cofeeshop.entity.Shop;
import com.cofeeshop.entity.ShopChain;
import com.cofeeshop.exception.ClientException;
import com.cofeeshop.repository.ContactRepository;
import com.cofeeshop.repository.DishRepository;
import com.cofeeshop.repository.ReservedOrderQueueRepository;
import com.cofeeshop.repository.ShopChainRepository;
import com.cofeeshop.repository.ShopRepository;
import com.cofeeshop.service.ShopService;
import com.cofeeshop.util.UserUtils;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ShopChainRepository shopChainRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ReservedOrderQueueRepository reservedOrderQueueRepository;
    
    @Override
    @Transactional
    public void deleteShop(Shop shop) {
        checkCurrentUserCanManageShop(shop);
        long shopId = shop.getId();
        List<ReservedOrderQueue> shopQueues = reservedOrderQueueRepository.findByShopId(shopId);
        shopQueues.stream().forEach(shopQueue -> {
            shopQueue.setStatus(DELETED);
            reservedOrderQueueRepository.save(shopQueue);
        });
        dishRepository.findByShopId(shopId).stream().forEach(dish -> {
            dish.setStatus(DELETED);
            dishRepository.save(dish);
        });
        contactRepository.findByShopId(shopId).stream().forEach(contact -> {
            contact.setStatus(DELETED);
            contactRepository.save(contact);
        });
        shop.setStatus(DELETED);
        shopRepository.save(shop);
    }

    @Override
    public ShopDTO getShopDetail(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        checkCurrentUserCanManageShop(shop);

        ShopDTO shopDTO = new ShopDTO(shop);

        List<DishDTO> distDtoList = dishRepository.findByShopId(shopId).stream().map(DishDTO::new).collect(Collectors.toList());
        shopDTO.setDishs(distDtoList);

        List<ContactDTO> contactDtoList = contactRepository.findByShopId(shopId).stream().map(ContactDTO::new).collect(Collectors.toList());
        shopDTO.setContacts(contactDtoList);

        List<ReservedOrderQueueDTO> reservedOrderQueueList = 
                reservedOrderQueueRepository.findByShopId(shopId)
                                            .stream()
                                            .map(ReservedOrderQueueDTO::new)
                                            .collect(Collectors.toList());
        shopDTO.setReservedOrderQueues(reservedOrderQueueList);
        
        return shopDTO;
    }

    @Override
    public void updateShopInfo(Long shopId, ShopDTO shopDTO) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        checkCurrentUserCanManageShop(shop);
        Optional.ofNullable(shopDTO.getShopOperator()).ifPresent(shop::setShopOperator);
        Optional.ofNullable(shopDTO.getOpenTime()).ifPresent(shop::setOpenTime);
        Optional.ofNullable(shopDTO.getCloseTime()).ifPresent(shop::setCloseTime);
        Optional.ofNullable(shopDTO.getLongitude()).ifPresent(shop::setLongitude);
        Optional.ofNullable(shopDTO.getLatitude()).ifPresent(shop::setLatitude);
        shopRepository.save(shop);
    }

    @Override
    public void checkCurrentUserCanManageShop(Shop shop) {
        String currentUserName = userUtils.retrieveCurrentUser();
        String shopOperatorUsername = shop.getShopOperator();
        ShopChain shopChain = shopChainRepository.findById(shop.getShopChainId()).orElseThrow(() -> new ClientException(SHOP_CHAIN_NOT_FOUND));
        String shopChainOwnerUsername = shopChain.getShopChainOwner();

        List.of(shopOperatorUsername, shopChainOwnerUsername)
            .stream().filter(currentUserName::equals)
            .findFirst()
            .orElseThrow(() -> new ClientException(NOT_ENOUGH_PERMISSION));

    }

    @Override
    public Long addDishToShop(Long shopId, DishDTO dishDTO) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        Optional.ofNullable(shop.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(SHOP_ALREADY_DELETED));
        checkCurrentUserCanManageShop(shop);
        Dish createdDish = new Dish();
        BeanUtils.copyProperties(dishDTO, createdDish);
        createdDish.setShopId(shopId);
        createdDish.setStatus(AVAILABLE);
        return dishRepository.save(createdDish).getId();
    }

    @Override
    public void removeDishFromShop(Long shopId, Long dishId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        checkCurrentUserCanManageShop(shop);
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ClientException(DISH_NOT_FOUND));
        dish.setStatus(DELETED);
        dishRepository.save(dish);
        
    }

    @Override
    public void addContactToShop(Long shopId, ContactDTO contactDTO) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        Optional.ofNullable(shop.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(SHOP_ALREADY_DELETED));
        checkCurrentUserCanManageShop(shop);
        Contact createdContact = new Contact();
        BeanUtils.copyProperties(contactDTO, createdContact);
        createdContact.setShopId(shopId);
        createdContact.setStatus(AVAILABLE);
        contactRepository.save(createdContact);
    }

    @Override
    public void removeContactFromShop(Long shopId, Long contactId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        checkCurrentUserCanManageShop(shop);
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new ClientException(CONTACT_NOT_FOUND));
        contact.setStatus(DELETED);
        contactRepository.save(contact);
    }

    @Override
    public Long addOrderQueueToShop(Long shopId, ReservedOrderQueueDTO queueDTO) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        Optional.ofNullable(shop.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(SHOP_ALREADY_DELETED));
        checkCurrentUserCanManageShop(shop);
        ReservedOrderQueue createdOrderQueue = new ReservedOrderQueue();
        BeanUtils.copyProperties(queueDTO, createdOrderQueue);
        createdOrderQueue.setShopId(shopId);
        createdOrderQueue.setStatus(AVAILABLE);
        return reservedOrderQueueRepository.save(createdOrderQueue).getId();
    }

    @Override
    public void removeOrderQueueFromShop(Long shopId, Long orderQueueId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        checkCurrentUserCanManageShop(shop);
        ReservedOrderQueue orderQueue = 
            reservedOrderQueueRepository.findById(orderQueueId).orElseThrow(() -> new ClientException(ORDER_QUEUE_NOT_FOUND));
        orderQueue.setStatus(DELETED);
        reservedOrderQueueRepository.save(orderQueue);
    }

    @Override
    public List<ShopDTO> findNearestShops(Double longitude, Double latitude, Integer numberOfShops) {
        List<Shop> nearestShops = shopRepository.findNearestShops(longitude, latitude, numberOfShops);
        Function<Shop, ShopDTO> createShopDTO = shop -> {
            ShopDTO shopDTO = new ShopDTO(shop);
            shopDTO.setShopOperator(null);
            ShopChain shopChain = shopChainRepository.findById(shop.getShopChainId()).orElseThrow(() -> new ClientException(Constants.SHOP_CHAIN_NOT_FOUND));
            shopDTO.setShopChain(ShopChainDTO.builder().shopChainName(shopChain.getShopChainName()).build());
            return shopDTO;
        };
        return nearestShops.stream().map(createShopDTO::apply).collect(Collectors.toList());
    }

    
    
}
