package com.filimonov.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.filimonov.shoppinglist.R
import com.filimonov.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.updatePadding(
                left = systemBars.left,
                top = maxOf(systemBars.top, imeInsets.top),
                right = systemBars.right,
                bottom = maxOf(systemBars.bottom, imeInsets.bottom)
            )
            insets
        }
        parseIntent()
        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        observeViewModel()
        addTextChangedListeners()
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun addTextChangedListeners() {
        etName.doOnTextChanged { _, _, _, _ ->
            viewModel.resetErrorInputName()
        }
        etCount.doOnTextChanged { _, _, _, _ ->
            viewModel.resetErrorInputCount()
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.invalid_name)
            } else {
                null
            }
            tilName.error = message
        }
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.invalid_name)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.isFinished.observe(this) {
            finish()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            val name = etName.text.toString().trim()
            val count = etCount.text.toString().trim()
            viewModel.editShopItem(name, count)
        }
    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            val name = etName.text.toString().trim()
            val count = etCount.text.toString().trim()
            viewModel.addShopItem(name, count)
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_MODE_SCREEN)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_MODE_SCREEN)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param item shop id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        saveButton = findViewById(R.id.save_button)
    }

    companion object {

        private const val EXTRA_MODE_SCREEN = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""


        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE_SCREEN, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE_SCREEN, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}