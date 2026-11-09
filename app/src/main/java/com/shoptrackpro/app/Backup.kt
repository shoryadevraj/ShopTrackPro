package com.shoptrackpro.app

data class BackupBundle(
 val formatVersion:Int=1,val appVersion:String="2.2.3",val exportedAt:Long=System.currentTimeMillis(),
 val shops:List<Shop>,val catalog:List<CatalogProduct>,val shopProducts:List<ShopProduct>,
 val prices:List<PriceOption>,val priceHistory:List<PriceHistory>,val balances:List<StockBalance>,
 val movements:List<StockMovement>,val sales:List<Sale>,val saleItems:List<SaleItem>
)
