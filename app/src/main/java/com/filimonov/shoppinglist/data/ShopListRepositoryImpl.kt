package com.filimonov.shoppinglist.data

import com.filimonov.shoppinglist.domain.ShopItem
import com.filimonov.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val shopItems = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val shopItem = ShopItem("Name $i", i, true)
            shopItems.add(shopItem)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopItems.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItems.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        deleteShopItem(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return shopItems.find {
            it.id == itemId
        } ?: throw RuntimeException("Shop item with id $itemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopItems.toList()
    }
}