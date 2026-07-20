package com.seniorease.library.utils

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_PREMIUM = "is_premium"
        const val PRODUCT_ID = "premium_unlock"
        const val FREE_ITEM_LIMIT = 10
    }

    private val _isPremium = MutableStateFlow(loadPremiumFromPrefs())
    val isPremiumFlow: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            val hasPremium = purchases.any { purchase ->
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    purchase.products.contains(PRODUCT_ID)
            }
            if (hasPremium) {
                purchases.forEach { handlePurchase(it) }
            } else {
                // No active premium purchase for this Play account
                savePremium(false)
            }
        }
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private fun loadPremiumFromPrefs(): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_PREMIUM, false)

    private fun savePremium(premium: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_PREMIUM, premium).apply()
        _isPremium.value = premium
    }

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryExistingPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    fun queryExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val hasPremium = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        purchase.products.contains(PRODUCT_ID)
                }
                // Always sync with Play: true if owned, false if not.
                // Prevents stale premium after account switch / backup restore.
                savePremium(hasPremium)
                if (hasPremium) {
                    purchases
                        .filter {
                            it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                                it.products.contains(PRODUCT_ID) &&
                                !it.isAcknowledged
                        }
                        .forEach { purchase ->
                            val ackParams = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()
                            billingClient.acknowledgePurchase(ackParams) {}
                        }
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
            purchase.products.contains(PRODUCT_ID)
        ) {
            savePremium(true)
            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(params) {}
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_ID)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        ) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                productDetailsList.isNotEmpty()
            ) {
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetailsList[0])
                                .build()
                        )
                    )
                    .build()
                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
        }
    }

    fun isPremium(): Boolean = _isPremium.value

    fun disconnect() {
        billingClient.endConnection()
    }
}
