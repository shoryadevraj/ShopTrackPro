package com.shoptrackpro.app

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName="shops")
data class Shop(@PrimaryKey(autoGenerate=true) val id:Long=0, val name:String, val address:String="", val phone:String="", val createdAt:Long=System.currentTimeMillis())

@Entity(tableName="catalog", indices=[Index("nameEn"),Index("nameHi"),Index("category"),Index("barcode")])
data class CatalogProduct(
 @PrimaryKey(autoGenerate=true) val id:Long=0,
 val nameEn:String, val nameHi:String, val category:String, val description:String="",
 val brand:String="", val defaultUnit:String="Piece", val barcode:String="", val aliases:String="",
 val imageUri:String=""
)

@Entity(tableName="shop_products",
 foreignKeys=[ForeignKey(entity=Shop::class,parentColumns=["id"],childColumns=["shopId"],onDelete=ForeignKey.CASCADE),
 ForeignKey(entity=CatalogProduct::class,parentColumns=["id"],childColumns=["catalogId"],onDelete=ForeignKey.RESTRICT)],
 indices=[Index("shopId"),Index("catalogId"),Index(value=["shopId","catalogId"],unique=true)])
data class ShopProduct(@PrimaryKey(autoGenerate=true) val id:Long=0,val shopId:Long,val catalogId:Long,
 val displayNameEn:String="",val displayNameHi:String="",val description:String="",val available:Boolean=true)

@Entity(tableName="price_options",foreignKeys=[ForeignKey(entity=ShopProduct::class,parentColumns=["id"],childColumns=["shopProductId"],onDelete=ForeignKey.CASCADE)],indices=[Index("shopProductId")])
data class PriceOption(@PrimaryKey(autoGenerate=true) val id:Long=0,val shopProductId:Long,val quantity:Double,val unit:String,
 val purchasePaise:Long,val sellingPaise:Long,val updatedAt:Long=System.currentTimeMillis())

@Entity(tableName="price_history",indices=[Index("priceOptionId")])
data class PriceHistory(@PrimaryKey(autoGenerate=true) val id:Long=0,val priceOptionId:Long,val purchasePaise:Long,val sellingPaise:Long,val changedAt:Long=System.currentTimeMillis())

@Entity(tableName="stock_balances",indices=[Index(value=["shopProductId"],unique=true)])
data class StockBalance(@PrimaryKey(autoGenerate=true) val id:Long=0,val shopProductId:Long,val quantity:Double=0.0,val unit:String="Piece",val minimum:Double=0.0,val updatedAt:Long=System.currentTimeMillis())

@Entity(tableName="stock_movements",indices=[Index("shopProductId"),Index("createdAt")])
data class StockMovement(@PrimaryKey(autoGenerate=true) val id:Long=0,val shopProductId:Long,val type:String,val quantity:Double,val unit:String,
 val unitCostPaise:Long=0,val note:String="",val createdAt:Long=System.currentTimeMillis())

@Entity(tableName="sales",indices=[Index("shopId"),Index("createdAt")])
data class Sale(@PrimaryKey(autoGenerate=true) val id:Long=0,val shopId:Long,val totalPaise:Long,val totalCostPaise:Long,val note:String="",val createdAt:Long=System.currentTimeMillis())

@Entity(tableName="sale_items",indices=[Index("saleId"),Index("shopProductId")])
data class SaleItem(@PrimaryKey(autoGenerate=true) val id:Long=0,val saleId:Long,val shopProductId:Long,val productName:String,
 val quantity:Double,val unit:String,val costPaise:Long,val sellingPaise:Long)

@Dao
interface StoreDao {
 @Query("SELECT * FROM shops ORDER BY name") suspend fun getShops():List<Shop>
 @Insert suspend fun insertShop(x:Shop):Long
 @Update suspend fun updateShop(x:Shop)
 @Delete suspend fun deleteShop(x:Shop)
 @Query("SELECT * FROM catalog ORDER BY category,nameEn") suspend fun getCatalog():List<CatalogProduct>
 @Query("DELETE FROM catalog WHERE description LIKE '%Pack size:%' AND id NOT IN (SELECT catalogId FROM shop_products)") suspend fun deleteUnusedPackCatalogRows()
 @Query("SELECT * FROM catalog WHERE nameEn LIKE '%'||:q||'%' OR nameHi LIKE '%'||:q||'%' OR category LIKE '%'||:q||'%' OR aliases LIKE '%'||:q||'%' OR barcode LIKE '%'||:q||'%' ORDER BY category,nameEn") suspend fun searchCatalog(q:String):List<CatalogProduct>
 @Query("SELECT * FROM catalog WHERE barcode=:code LIMIT 1") suspend fun byBarcode(code:String):CatalogProduct?
 @Insert(onConflict=OnConflictStrategy.IGNORE) suspend fun insertCatalog(items:List<CatalogProduct>)
 @Insert suspend fun insertCatalogOne(x:CatalogProduct):Long
 @Update suspend fun updateCatalog(x:CatalogProduct)
 @Query("SELECT * FROM shop_products WHERE shopId=:shopId ORDER BY id DESC") suspend fun getShopProducts(shopId:Long):List<ShopProduct>
 @Query("SELECT * FROM shop_products WHERE id=:id LIMIT 1") suspend fun getShopProduct(id:Long):ShopProduct?
 @Insert(onConflict=OnConflictStrategy.IGNORE) suspend fun addShopProduct(x:ShopProduct):Long
 @Update suspend fun updateShopProduct(x:ShopProduct)
 @Delete suspend fun deleteShopProduct(x:ShopProduct)
 @Query("DELETE FROM price_history WHERE priceOptionId IN (SELECT id FROM price_options WHERE shopProductId=:id)") suspend fun deleteProductPriceHistory(id:Long)
 @Query("DELETE FROM stock_movements WHERE shopProductId=:id") suspend fun deleteProductMovements(id:Long)
 @Query("DELETE FROM stock_balances WHERE shopProductId=:id") suspend fun deleteProductBalance(id:Long)
 @Query("DELETE FROM price_options WHERE shopProductId=:id") suspend fun deleteProductPrices(id:Long)
 @Query("SELECT * FROM price_options WHERE shopProductId=:id ORDER BY quantity,unit") suspend fun getPrices(id:Long):List<PriceOption>
 @Insert suspend fun addPrice(x:PriceOption):Long
 @Update suspend fun updatePrice(x:PriceOption)
 @Delete suspend fun deletePrice(x:PriceOption)
 @Insert suspend fun addPriceHistory(x:PriceHistory)
 @Query("SELECT * FROM price_history WHERE priceOptionId=:id ORDER BY changedAt DESC") suspend fun priceHistory(id:Long):List<PriceHistory>
 @Query("SELECT * FROM stock_balances WHERE shopProductId=:id LIMIT 1") suspend fun getBalance(id:Long):StockBalance?
 @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun saveBalance(x:StockBalance)
 @Insert suspend fun addMovement(x:StockMovement)
 @Query("SELECT * FROM stock_movements WHERE shopProductId=:id ORDER BY createdAt DESC") suspend fun getMovements(id:Long):List<StockMovement>
 @Insert suspend fun addSale(x:Sale):Long
 @Insert suspend fun addSaleItems(xs:List<SaleItem>)
 @Query("SELECT * FROM sales WHERE shopId=:id ORDER BY createdAt DESC") suspend fun getSales(id:Long):List<Sale>
 @Query("SELECT * FROM sale_items WHERE saleId=:id") suspend fun getSaleItems(id:Long):List<SaleItem>
 @Query("SELECT * FROM sale_items WHERE saleId IN (SELECT id FROM sales WHERE shopId=:shopId)") suspend fun allSaleItems(shopId:Long):List<SaleItem>
 @Query("SELECT * FROM stock_movements WHERE shopProductId IN (SELECT id FROM shop_products WHERE shopId=:shopId) ORDER BY createdAt DESC") suspend fun shopMovements(shopId:Long):List<StockMovement>
 @Query("DELETE FROM sale_items") suspend fun clearSaleItems()
 @Query("DELETE FROM sales") suspend fun clearSales()
 @Query("DELETE FROM stock_movements") suspend fun clearMovements()
 @Query("DELETE FROM stock_balances") suspend fun clearBalances()
 @Query("DELETE FROM price_history") suspend fun clearPriceHistory()
 @Query("DELETE FROM price_options") suspend fun clearPrices()
 @Query("DELETE FROM shop_products") suspend fun clearShopProducts()
 @Query("DELETE FROM shops") suspend fun clearShops()
 @Query("DELETE FROM catalog") suspend fun clearCatalog()
 @Insert suspend fun importShops(xs:List<Shop>)
 @Insert suspend fun importCatalog(xs:List<CatalogProduct>)
 @Insert suspend fun importShopProducts(xs:List<ShopProduct>)
 @Insert suspend fun importPrices(xs:List<PriceOption>)
 @Insert suspend fun importPriceHistory(xs:List<PriceHistory>)
 @Insert suspend fun importBalances(xs:List<StockBalance>)
 @Insert suspend fun importMovements(xs:List<StockMovement>)
 @Insert suspend fun importSales(xs:List<Sale>)
 @Insert suspend fun importSaleItems(xs:List<SaleItem>)
}

@Database(entities=[Shop::class,CatalogProduct::class,ShopProduct::class,PriceOption::class,PriceHistory::class,StockBalance::class,StockMovement::class,Sale::class,SaleItem::class],version=1,exportSchema=true)
abstract class StoreDb:RoomDatabase(){
 abstract fun dao():StoreDao
 companion object {
  @Volatile private var instance:StoreDb?=null
  fun get(c:Context):StoreDb=instance?: synchronized(this){instance?:Room.databaseBuilder(c.applicationContext,StoreDb::class.java,"shoptrack_pro.db").build().also{instance=it}}
 }
}
