package com.filimonov.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filimonov.shoppinglist.data.ShopListRepositoryImpl
import com.filimonov.shoppinglist.domain.DeleteShopItemUseCase
import com.filimonov.shoppinglist.domain.EditShopItemUseCase
import com.filimonov.shoppinglist.domain.GetShopItemUseCase
import com.filimonov.shoppinglist.domain.GetShopListUseCase
import com.filimonov.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        val list = getShopListUseCase.getShopList()
        shopList.value = list
    }

    fun changeEnabledState(shopItem: ShopItem) {
        val editedShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(editedShopItem)
        getShopList()
    }

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }
}