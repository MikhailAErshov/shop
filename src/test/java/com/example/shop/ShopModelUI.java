package com.example.shop;

import java.util.Objects;

public class ShopModelUI {
    private String id;
    private String shopName;
    private String isPublic;

    public ShopModelUI(String id, String shopName, String isPublic) {
        this.id = id;
        this.shopName = shopName;
        this.isPublic = isPublic;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopModelUI that = (ShopModelUI) o;
        return Objects.equals(id, that.id) && Objects.equals(shopName, that.shopName) && Objects.equals(isPublic, that.isPublic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopName, isPublic);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}
