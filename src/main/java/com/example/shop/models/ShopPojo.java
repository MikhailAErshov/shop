package com.example.shop.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;

@Entity
@Table(name = "shops", uniqueConstraints = {@UniqueConstraint(columnNames = "shop_id")})
public class ShopPojo {

    private Long shopId;

    private String shopName;

    private boolean shopPublic;

    public void setShopId(Long id) {
        this.shopId = id;
    }

    @Id
    @Column(name = "shop_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getShopId() {
        return shopId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Column(name = "shop_name", length = 256, nullable = false)
    public String getShopName() {
        return shopName;
    }

    public void setShopPublic(boolean shopPublic) {
        this.shopPublic = shopPublic;
    }

    @Column(name = "shop_public",  nullable = false)
    public boolean getShopPublic() {
        return shopPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopPojo shopPojo = (ShopPojo) o;
        return shopPublic == shopPojo.shopPublic && Objects.equals(shopId, shopPojo.shopId) && Objects.equals(shopName, shopPojo.shopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopId, shopName, shopPublic);
    }
}
