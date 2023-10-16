package com.cofeeshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cofeeshop.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByShopChainId(Long shopChainId);

    @Query(value = "SELECT *, ST_Distance_Sphere(ST_GeomFromText(CONCAT('Point(', longitude, ' ', latitude, ')')), ST_GeomFromText(CONCAT('Point(', :paramLongitude, ' ', :paramLatitude, ')'))) as distance FROM shop order by distance limit :numberOfShops" , nativeQuery=true)
    List<Shop> findNearestShops(@Param("paramLongitude") Double longitude, @Param("paramLatitude") Double latitude, @Param("numberOfShops") Integer numberOfShops);
}
