package com.filimonov.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.filimonov.shoppinglist.R
import com.filimonov.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val layout = when(viewType) {
            VIEW_ENABLED -> R.layout.item_shop_enabled
            VIEW_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopListViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.enabled) {
            VIEW_ENABLED
        } else {
            VIEW_DISABLED
        }
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    companion object {
        const val VIEW_ENABLED = 1
        const val VIEW_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }
}