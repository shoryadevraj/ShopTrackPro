package com.shoptrackpro.app

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.room.withTransaction
import com.google.gson.GsonBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{ShopTrackApp(StoreDb.get(this))}}
}
private val violet=Color(0xFF7952D6)
private val mint=Color(0xFF36C78A)
private fun money(p:Long)="₹"+"%.2f".format(Locale.US,p/100.0)
private fun rupeesToPaise(s:String)=((s.toDoubleOrNull()?:0.0)*100).toLong()
private fun stamp(t:Long)=SimpleDateFormat("dd MMM yyyy, hh:mm a",Locale.getDefault()).format(Date(t))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopTrackApp(db:StoreDb){
 val ctx= LocalContext.current; val dao=db.dao(); val pin= remember{PinManager(ctx)}; val scope= rememberCoroutineScope()
 var owner by remember{mutableStateOf(false)}; var showPin by remember{mutableStateOf(!pin.exists())}; var setupPin by remember{mutableStateOf(!pin.exists())}
 var pinText by remember{mutableStateOf("")}; var pinAgain by remember{mutableStateOf("")}; var pinError by remember{mutableStateOf("")}
 var shops by remember{mutableStateOf(emptyList<Shop>())}; var activeShop by remember{mutableStateOf<Shop?>(null)}
 var catalog by remember{mutableStateOf(emptyList<CatalogProduct>())}; var products by remember{mutableStateOf(emptyList<ShopProduct>())}
 var page by remember{mutableStateOf("shops")}; var search by remember{mutableStateOf("")}; var notice by remember{mutableStateOf("")}; var showExitDialog by remember{mutableStateOf(false)}
 var selected by remember{mutableStateOf<ShopProduct?>(null)}; var confirmDeleteProduct by remember{mutableStateOf(false)}; var selectedCatalog by remember{mutableStateOf<CatalogProduct?>(null)}
 var editCatalog by remember{mutableStateOf<CatalogProduct?>(null)}; var showProductForm by remember{mutableStateOf(false)}
 var showShopForm by remember{mutableStateOf(false)}; var editShop by remember{mutableStateOf<Shop?>(null)}
 var nameEn by remember{mutableStateOf("")}; var brand by remember{mutableStateOf("")}; var nameHi by remember{mutableStateOf("")}; var nameHinglish by remember{mutableStateOf("")}; var category by remember{mutableStateOf("")}; var desc by remember{mutableStateOf("")}; var barcode by remember{mutableStateOf("")}; var unit by remember{mutableStateOf("Piece")}
 var shopName by remember{mutableStateOf("")}; var shopAddress by remember{mutableStateOf("")}; var shopPhone by remember{mutableStateOf("")}
 var qty by remember{mutableStateOf("1")}; var priceUnit by remember{mutableStateOf("Piece")}; var buy by remember{mutableStateOf("")}; var sell by remember{mutableStateOf("")}
 var stockQty by remember{mutableStateOf("")}; var stockMin by remember{mutableStateOf("0")}; var stockUnit by remember{mutableStateOf("Piece")}; var movement by remember{mutableStateOf("IN")}; var movementNote by remember{mutableStateOf("")}
 var saleQty by remember{mutableStateOf("1")}; var saleNote by remember{mutableStateOf("")}
 var calcBuy by remember{mutableStateOf("")}; var calcSell by remember{mutableStateOf("")}; var calcQty by remember{mutableStateOf("1")}
 var calculator by remember{mutableStateOf("Profit")}; var arithmetic by remember{mutableStateOf("")}
 var showPriceForm by remember{mutableStateOf(false)}; var showStockForm by remember{mutableStateOf(false)}; var showSaleForm by remember{mutableStateOf(false)}
 var priceOptions by remember{mutableStateOf(emptyList<PriceOption>())}; var balance by remember{mutableStateOf<StockBalance?>(null)}; var movements by remember{mutableStateOf(emptyList<StockMovement>())}; var histories by remember{mutableStateOf(emptyList<PriceHistory>())}; var sales by remember{mutableStateOf(emptyList<Sale>())}
 var currentPrice by remember{mutableStateOf<PriceOption?>(null)}; var editPrice by remember{mutableStateOf<PriceOption?>(null)}
 var ocr by remember{mutableStateOf("")}; var cameraBitmap by remember{mutableStateOf<Bitmap?>(null)}
 fun refreshShops(){scope.launch{shops=dao.getShops();if(activeShop==null)activeShop=shops.firstOrNull()}}
 fun refreshProducts(){scope.launch{activeShop?.let{products=dao.getShopProducts(it.id)}}}
 fun refreshDetail(){scope.launch{selected?.let{priceOptions=dao.getPrices(it.id);balance=dao.getBalance(it.id);movements=dao.getMovements(it.id)}}}
 BackHandler(enabled=true){
  when {
   showExitDialog -> showExitDialog=false
   showProductForm -> showProductForm=false
   showShopForm -> showShopForm=false
   showPriceForm -> showPriceForm=false
   showStockForm -> showStockForm=false
   showSaleForm -> showSaleForm=false
   showPin -> { if(pin.exists()){showPin=false;pinText="";pinError=""} else showExitDialog=true }
   confirmDeleteProduct -> confirmDeleteProduct=false
   page=="history" -> page="detail"
   page=="detail" -> {selected=null;page="products"}
   page=="catalog" -> page="products"
   page=="products" -> page="shops"
   page!="shops" -> page="shops"
   else -> showExitDialog=true
  }
 }
 LaunchedEffect(Unit){
  dao.deleteUnusedPackCatalogRows()
  val existing=dao.getCatalog()
  val templates=StarterCatalog.items.associateBy{it.nameEn.lowercase(Locale.ROOT)}
  val known=existing.map{it.nameEn.lowercase(Locale.ROOT)}.toSet()
  dao.insertCatalog(StarterCatalog.items.filter{it.nameEn.lowercase(Locale.ROOT) !in known})
  // Backfill Hinglish aliases for catalog rows already installed on this phone.
  existing.forEach{old->val template=templates[old.nameEn.lowercase(Locale.ROOT)];if(template!=null&&template.aliases.isNotBlank()){val merged=(old.aliases.split(",").map{it.trim()}+template.aliases.split(",").map{it.trim()}).filter{it.isNotBlank()}.distinct().joinToString(", ");if(merged!=old.aliases)dao.updateCatalog(old.copy(aliases=merged))}}
  catalog=dao.getCatalog();refreshShops()
 }
 LaunchedEffect(activeShop?.id){refreshProducts()}
 LaunchedEffect(selected?.id){refreshDetail()}
 val barcodeLauncher=rememberLauncherForActivityResult(ScanContract()){r->
  if(r.contents!=null)scope.launch{
   val match=dao.byBarcode(r.contents!!)
   if(match!=null){selectedCatalog=match;showProductForm=true;editCatalog=match;nameEn=match.nameEn;brand=match.brand;nameHi=match.nameHi;nameHinglish=match.aliases;category=match.category;desc=match.description;barcode=match.barcode;unit=match.defaultUnit;notice="Barcode match found. Confirm details."}
   else{barcode=r.contents!!;showProductForm=true;editCatalog=null;nameEn="";brand="";nameHi="";nameHinglish="";category="";desc="";unit="Piece";notice="Unknown barcode. Complete product details to add it to the catalog."}
  }
 }
 val photoLauncher=rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){bmp->
  if(bmp!=null){cameraBitmap=bmp;val rec=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
   rec.process(InputImage.fromBitmap(bmp,0)).addOnSuccessListener{res->
    ocr=res.text;val first=res.text.lineSequence().firstOrNull()?.take(60).orEmpty()
    if(first.isNotBlank())nameEn=first
    showProductForm=true;editCatalog=null;notice="Label text extracted. Verify and complete product information."
    rec.close()
   }.addOnFailureListener{notice="Could not read label text.";rec.close()}
  }
 }
 val backupOut=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")){uri->
  if(uri!=null)scope.launch{try{
   val allShops=dao.getShops();val allCatalog=dao.getCatalog()
   val allSP=allShops.flatMap{dao.getShopProducts(it.id)}
   val allPrices=allSP.flatMap{dao.getPrices(it.id)}
   val allBalances=allSP.mapNotNull{dao.getBalance(it.id)}
   val allMovements=allSP.flatMap{dao.getMovements(it.id)}
   val allSales=allShops.flatMap{dao.getSales(it.id)}
   val allItems=allSales.flatMap{dao.getSaleItems(it.id)}
   val allHistory=allPrices.flatMap{dao.priceHistory(it.id)}
   val bundle=BackupBundle(shops=allShops,catalog=allCatalog,shopProducts=allSP,prices=allPrices,priceHistory=allHistory,balances=allBalances,movements=allMovements,sales=allSales,saleItems=allItems)
   ctx.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use{it.write(GsonBuilder().setPrettyPrinting().create().toJson(bundle))}
   notice="Backup exported successfully."
  }catch(e:Exception){notice="Backup failed: ${e.localizedMessage}"}}
 }
 val backupIn=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri->
  if(uri!=null)scope.launch{try{
   val text=ctx.contentResolver.openInputStream(uri)?.bufferedReader()?.use{it.readText()}?:error("Cannot read backup")
   val b=com.google.gson.Gson().fromJson(text,BackupBundle::class.java)?:error("Invalid backup")
   require(b.formatVersion==1){"Unsupported backup version"}
   db.withTransaction{
    dao.clearSaleItems();dao.clearSales();dao.clearMovements();dao.clearBalances();dao.clearPriceHistory();dao.clearPrices();dao.clearShopProducts();dao.clearShops();dao.clearCatalog()
    dao.importCatalog(b.catalog);dao.importShops(b.shops);dao.importShopProducts(b.shopProducts);dao.importPrices(b.prices)
    dao.importPriceHistory(b.priceHistory);dao.importBalances(b.balances);dao.importMovements(b.movements);dao.importSales(b.sales);dao.importSaleItems(b.saleItems)
   }
   activeShop=null;refreshShops();catalog=dao.getCatalog();notice="Backup restored successfully."
  }catch(e:Exception){notice="Restore failed: ${e.localizedMessage}"}}
 }
 MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFFBCA6FF),secondary=mint,background=Color(0xFF171321),surface=Color(0xFF292436),surfaceVariant=Color(0xFF393247))){
 Scaffold(containerColor=Color(0xFF171321),topBar={TopAppBar(navigationIcon={if(page!="shops")IconButton(onClick={when(page){"history"->page="detail";"detail","catalog"->page="products";else->page="shops"}}){Icon(Icons.Default.ArrowBack,"Back")}},title={Row(verticalAlignment=Alignment.CenterVertically){
  Image(painterResource(R.drawable.shoptrack_logo),contentDescription="ShopTrack Pro logo",modifier=Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)),contentScale=ContentScale.Crop)
  Spacer(Modifier.width(10.dp));Column{Text("ShopTrack Pro",fontWeight=FontWeight.Bold);Text(if(owner)"Owner mode" else "Public browsing",style=MaterialTheme.typography.labelSmall)}
 }},actions={IconButton(onClick={if(owner){owner=false;page="shops"}else{showPin=true;setupPin=false;pinText="";pinError=""}}){Icon(if(owner)Icons.Default.LockOpen else Icons.Default.Lock,null)}},colors=TopAppBarDefaults.topAppBarColors(containerColor=Color(0xFF171321)))},
 bottomBar={NavigationBar(containerColor=Color(0xFF292436)){
  listOf("shops" to Icons.Default.Store,"products" to Icons.Default.Inventory2,"sales" to Icons.Default.ReceiptLong,"calculator" to Icons.Default.Calculate,"settings" to Icons.Default.Settings).forEach{(p,ic)->NavigationBarItem(selected=page==p,onClick={page=p},icon={Icon(ic,null)},label={Text(p.replaceFirstChar{it.uppercase()})})}
 }}){pad->
 Column(Modifier.fillMaxSize().padding(pad).background(Brush.verticalGradient(listOf(Color(0xFF211A30),Color(0xFF171321)))).padding(14.dp)){
  if(notice.isNotBlank()){Text(notice,color=mint,style=MaterialTheme.typography.bodySmall);Spacer(Modifier.height(6.dp))}
  when(page){
   "shops"->{
    Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Shops",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text("Select a shop to browse its products.",color=Color.LightGray)};if(owner)IconButton(onClick={editShop=null;shopName="";shopAddress="";shopPhone="";showShopForm=true}){Icon(Icons.Default.AddBusiness,null)}}
    LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){items(shops,key={it.id}){s->Card(Modifier.fillMaxWidth().clickable{activeShop=s;page="products"}){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Storefront,null,tint=mint);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(s.name,fontWeight=FontWeight.Bold);Text(s.address,style=MaterialTheme.typography.bodySmall)};if(owner)IconButton(onClick={editShop=s;shopName=s.name;shopAddress=s.address;shopPhone=s.phone;showShopForm=true}){Icon(Icons.Default.Edit,null)};Icon(Icons.Default.ChevronRight,null)}}}}
    if(owner)Button(onClick={editShop=null;shopName="";shopAddress="";shopPhone="";showShopForm=true},Modifier.fillMaxWidth()){Icon(Icons.Default.Add,null);Text(" Create shop")}
   }
   "products"->{
    Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(activeShop?.name?: "Products",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text(if(owner)"Manage this shop's products" else "Read-only product list",style=MaterialTheme.typography.bodySmall)};if(owner){IconButton(onClick={barcodeLauncher.launch(ScanOptions().setPrompt("Scan product barcode").setBeepEnabled(false).setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES))}){Icon(Icons.Default.QrCodeScanner,"Scan barcode")};IconButton(onClick={photoLauncher.launch(null)}){Icon(Icons.Default.PhotoCamera,"Scan label")}}}
    OutlinedTextField(search,{search=it},Modifier.fillMaxWidth(),label={Text("Search English, Hindi, Hinglish, category or barcode")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true)
    if(owner)Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Button(onClick={showProductForm=true;editCatalog=null;nameEn="";brand="";nameHi="";nameHinglish="";category="";desc="";barcode="";unit="Piece"},Modifier.weight(1f)){Icon(Icons.Default.Add,null);Text("Add product")};OutlinedButton(onClick={page="catalog"},Modifier.weight(1f)){Text("Browse catalog")}}
    LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){items(products.filter{sp->val c=catalog.firstOrNull{it.id==sp.catalogId};c!=null&&(search.isBlank()||listOf(c.nameEn,c.nameHi,c.category,c.aliases,c.barcode).any{it.contains(search,true)})},key={it.id}){sp->
     val c=catalog.firstOrNull{it.id==sp.catalogId}
     if(c!=null)ProductTile(sp,c,dao,owner){selected=sp;page="detail"}
    }}
   }
   "catalog"->{
    Text("Master product catalog",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    OutlinedTextField(search,{search=it},Modifier.fillMaxWidth(),label={Text("Search English, Hindi or Hinglish (e.g. Pashu Aahar)")},singleLine=true)
    LazyColumn(verticalArrangement=Arrangement.spacedBy(6.dp)){items(catalog.filter{!it.description.contains("Pack size:") && (search.isBlank()||listOf(it.nameEn,it.nameHi,it.category,it.aliases,it.barcode,it.brand).any{s->s.contains(search,true)})}){c->
     Card(Modifier.fillMaxWidth().clickable{if(!owner)notice="Owner access required." else scope.launch{val shop=activeShop;if(shop==null)notice="Create/select a shop first." else{val id=dao.addShopProduct(ShopProduct(shopId=shop.id,catalogId=c.id));if(id==-1L)notice="Product already exists in this shop." else notice="${c.nameEn} added.";refreshProducts();page="products"}}}){Column(Modifier.padding(12.dp)){Text(listOf(c.nameEn,c.nameHi,c.aliases).filter{it.isNotBlank()}.joinToString(" • "),fontWeight=FontWeight.Bold);Text(listOf(c.brand,c.category,c.description).filter{it.isNotBlank()}.joinToString(" · "),style=MaterialTheme.typography.bodySmall)}}
    }}
   }
   "detail"->{
    val sp=selected;val c=sp?.let{x->catalog.firstOrNull{it.id==x.catalogId}}
    if(sp==null||c==null)Text("Select a product.")
    else{
     Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(listOf((if(sp.displayNameEn.isBlank())c.nameEn else sp.displayNameEn),(if(sp.displayNameHi.isBlank())c.nameHi else sp.displayNameHi),c.aliases).filter{it.isNotBlank()}.joinToString(" • "),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text(listOf(c.brand,c.category,c.description).filter{it.isNotBlank()}.joinToString(" · "),style=MaterialTheme.typography.bodySmall)};if(owner){IconButton(onClick={editCatalog=c;nameEn=c.nameEn;brand=c.brand;nameHi=c.nameHi;nameHinglish=c.aliases;category=c.category;desc=c.description;barcode=c.barcode;unit=c.defaultUnit;showProductForm=true}){Icon(Icons.Default.Edit,null)};IconButton(onClick={confirmDeleteProduct=true}){Icon(Icons.Default.Delete,null,tint=MaterialTheme.colorScheme.error)}}}
     Spacer(Modifier.height(10.dp));Text("Price options",fontWeight=FontWeight.Bold)
     if(priceOptions.isEmpty())Text("No prices added.")
     priceOptions.forEach{p->Card(Modifier.fillMaxWidth().padding(vertical=3.dp)){Column(Modifier.padding(12.dp)){
      Text("${p.quantity} ${p.unit}",fontWeight=FontWeight.Bold);Text(money(p.sellingPaise),style=MaterialTheme.typography.titleLarge,color=MaterialTheme.colorScheme.primary)
      if(owner){if(p.purchasePaise>0){val diff=p.sellingPaise-p.purchasePaise;val pct=diff.toDouble()/p.purchasePaise*100.0;val margin=if(p.sellingPaise>0)diff.toDouble()/p.sellingPaise*100.0 else 0.0;Text("Purchase ${money(p.purchasePaise)} · ${if(diff>=0)"Profit" else "Loss"} ${money(kotlin.math.abs(diff))}");Text("${if(diff>=0)"Profit" else "Loss"} on cost: %.2f%% · Margin: %.2f%%".format(Locale.US,kotlin.math.abs(pct),kotlin.math.abs(margin)),style=MaterialTheme.typography.bodySmall)}else Text("Purchase price not recorded",style=MaterialTheme.typography.bodySmall)};if(owner)Row{TextButton(onClick={editPrice=p;qty=p.quantity.toString();priceUnit=p.unit;buy=(p.purchasePaise/100.0).toString();sell=(p.sellingPaise/100.0).toString();showPriceForm=true}){Text("Edit")};TextButton(onClick={scope.launch{dao.deletePrice(p);refreshDetail();notice="Price option deleted."}}){Text("Delete")};TextButton(onClick={currentPrice=p;histories=emptyList();scope.launch{histories=dao.priceHistory(p.id)};page="history"}){Text("History")}}
     }}}
     Text("Stock",fontWeight=FontWeight.Bold);Text(balance?.let{"${it.quantity} ${it.unit} · Minimum ${it.minimum}"}?:"Stock not recorded")
     if(owner){Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedButton(onClick={barcodeLauncher.launch(ScanOptions().setPrompt("Scan product barcode").setBeepEnabled(false).setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES))},Modifier.weight(1f)){Icon(Icons.Default.QrCodeScanner,null);Text(" Scan barcode")};Button(onClick={qty="1";priceUnit=c.defaultUnit;buy="";sell="";editPrice=null;showPriceForm=true},Modifier.weight(1f)){Text("Add price")}};Button(onClick={stockQty="";stockUnit=balance?.unit?:c.defaultUnit;showStockForm=true},Modifier.fillMaxWidth()){Text("Stock movement")};OutlinedButton(onClick={saleQty="1";showSaleForm=true},Modifier.fillMaxWidth()){Icon(Icons.Default.PointOfSale,null);Text(" Record sale")};Text("Recent stock movements",fontWeight=FontWeight.Bold);movements.take(10).forEach{Text("${it.type} ${it.quantity} ${it.unit} · ${stamp(it.createdAt)}",style=MaterialTheme.typography.bodySmall)}}
    }
   }
   "history"->{Text("Price history",style=MaterialTheme.typography.headlineSmall);histories.forEach{Text("${stamp(it.changedAt)} · Buy ${money(it.purchasePaise)} · Sell ${money(it.sellingPaise)}")}}
   "sales"->{
    if(!owner)Text("Sales history is available only in owner mode.",style=MaterialTheme.typography.bodyLarge)
    else{
    Text("Sales history",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    val sh=activeShop
    if(sh==null)Text("Select a shop first.") else{
     LaunchedEffect(sh.id){sales=dao.getSales(sh.id)}
     val revenue=sales.sumOf{it.totalPaise};val cost=sales.sumOf{it.totalCostPaise}
     Card(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text("Total sales: ${money(revenue)}",fontWeight=FontWeight.Bold);Text("Recorded gross profit: ${money(revenue-cost)}")}}
     LazyColumn{items(sales){s->Card(Modifier.fillMaxWidth().padding(vertical=4.dp)){Column(Modifier.padding(12.dp)){Text("Sale #${s.id} · ${stamp(s.createdAt)}",fontWeight=FontWeight.Bold);Text("Total ${money(s.totalPaise)} · Cost ${money(s.totalCostPaise)} · Profit ${money(s.totalPaise-s.totalCostPaise)}");Text(s.note,style=MaterialTheme.typography.bodySmall)}}}}
    }
    }
   }
   "calculator"->{
    Text("Calculators",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(selected=calculator=="Profit",onClick={calculator="Profit"},label={Text("Profit & loss")});FilterChip(selected=calculator=="Normal",onClick={calculator="Normal"},label={Text("Normal")})}
    if(calculator=="Profit"){
     OutlinedTextField(calcBuy,{calcBuy=it},Modifier.fillMaxWidth(),label={Text("Purchase price per unit ₹")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
     OutlinedTextField(calcSell,{calcSell=it},Modifier.fillMaxWidth(),label={Text("Selling price per unit ₹")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
     OutlinedTextField(calcQty,{calcQty=it},Modifier.fillMaxWidth(),label={Text("Quantity")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
     val b=calcBuy.toDoubleOrNull();val s=calcSell.toDoubleOrNull();val q=calcQty.toDoubleOrNull()
     if(b!=null&&s!=null&&q!=null&&b>0&&q>0){val diff=(s-b)*q;Text(if(diff>=0)"Profit: ₹%.2f".format(diff) else "Loss: ₹%.2f".format(-diff),style=MaterialTheme.typography.titleLarge,color=if(diff>=0)mint else Color.Red);Text("Profit on cost: %.2f%%".format((s-b)/b*100));if(s>0)Text("Margin on selling price: %.2f%%".format((s-b)/s*100))}
    }else{
     OutlinedTextField(arithmetic,{arithmetic=it},Modifier.fillMaxWidth(),label={Text("Example: 125 + 40 * 2")})
     Button(onClick={arithmetic=calculateExpression(arithmetic)?.let{"%.4f".format(it)}?:"Invalid expression"}){Text("Calculate")}
    }
   }
   "settings"->{
    Text("Settings",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    Text("Offline local storage · No cloud database · No online account")
    Text("Catalog entries: ${catalog.count{ !it.description.contains("Pack size:") }} · Shops: ${shops.size}")
    if(owner){
     Button(onClick={backupOut.launch("ShopTrackPro-backup.json")},Modifier.fillMaxWidth()){Icon(Icons.Default.Backup,null);Text(" Export full backup")}
     OutlinedButton(onClick={backupIn.launch(arrayOf("application/json"))},Modifier.fillMaxWidth()){Icon(Icons.Default.Restore,null);Text(" Restore backup")}
     OutlinedButton(onClick={showPin=true;setupPin=true;pinText="";pinAgain=""},Modifier.fillMaxWidth()){Icon(Icons.Default.Key,null);Text(" Change owner PIN")}
    }else Text("Unlock owner mode to access backup, restore and management.")
    Text("Designed & developed by Shorya Devraj",color=mint);Text("ShopTrack Pro v2.2.6 · © 2026")
    Text("Backup files contain private business information. Keep them secure.",style=MaterialTheme.typography.bodySmall,color=Color.LightGray)
   }
  }
 }
 }
 // Confirm exit from the main screen instead of closing on the first system Back press.
 if(showExitDialog)AlertDialog(
  onDismissRequest={showExitDialog=false},
  title={Text("Exit ShopTrack Pro?")},
  text={Text("Are you sure you want to close the app?")},
  confirmButton={TextButton(onClick={showExitDialog=false;(ctx as? ComponentActivity)?.finish()}){Text("Exit",color=MaterialTheme.colorScheme.error)}},
  dismissButton={TextButton(onClick={showExitDialog=false}){Text("Stay")}}
 )
 // Delete product from the current shop. Historical sale records remain as snapshots.
 if(confirmDeleteProduct&&selected!=null)AlertDialog(
  onDismissRequest={confirmDeleteProduct=false},
  title={Text("Delete product?")},
  text={Text("This removes the product, its prices, current stock and stock movement history from this shop. Past sales records are retained. This cannot be undone." )},
  confirmButton={TextButton(onClick={scope.launch{
   val sp=selected
   if(sp!=null){dao.deleteProductPriceHistory(sp.id);dao.deleteProductMovements(sp.id);dao.deleteProductBalance(sp.id);dao.deleteProductPrices(sp.id);dao.deleteShopProduct(sp);selected=null;priceOptions=emptyList();balance=null;movements=emptyList();refreshProducts();page="products";notice="Product deleted from this shop."}
   confirmDeleteProduct=false
  }}){Text("Delete",color=MaterialTheme.colorScheme.error)}},
  dismissButton={TextButton(onClick={confirmDeleteProduct=false}){Text("Cancel")}}
 )
 // Shop create/edit
 if(showShopForm)AlertDialog(onDismissRequest={showShopForm=false},title={Text(if(editShop==null)"Create shop" else "Edit shop")},text={Column{
  OutlinedTextField(shopName,{shopName=it},label={Text("Shop name")});OutlinedTextField(shopAddress,{shopAddress=it},label={Text("Address")});OutlinedTextField(shopPhone,{shopPhone=it},label={Text("Phone")})
 }},confirmButton={TextButton(onClick={scope.launch{
  if(shopName.isBlank())notice="Shop name is required." else{
   if(editShop==null){val id=dao.insertShop(Shop(name=shopName.trim(),address=shopAddress,phone=shopPhone));activeShop=Shop(id=id,name=shopName.trim(),address=shopAddress,phone=shopPhone)}
   else{dao.updateShop(editShop!!.copy(name=shopName.trim(),address=shopAddress,phone=shopPhone));if(activeShop?.id==editShop!!.id)activeShop=editShop!!.copy(name=shopName.trim(),address=shopAddress,phone=shopPhone)}
   refreshShops();showShopForm=false;notice="Shop saved."
  }
 }}){Text("Save")}},dismissButton={TextButton(onClick={showShopForm=false}){Text("Cancel")}})
 // Catalog product editor
 if(showProductForm)AlertDialog(onDismissRequest={showProductForm=false},title={Text(if(editCatalog==null)"Add catalog product" else "Edit catalog product")},text={Column(Modifier.heightIn(max=520.dp).verticalScroll(androidx.compose.foundation.rememberScrollState())){
  OutlinedTextField(nameEn,{nameEn=it},label={Text("Product name (English, optional)")});OutlinedTextField(brand,{brand=it},label={Text("Brand (optional)")});OutlinedTextField(nameHi,{nameHi=it},label={Text("Product name (Hindi, optional)")});OutlinedTextField(nameHinglish,{nameHinglish=it},label={Text("Product name (Hinglish / Roman Hindi, optional)")});Text("Fill any one, any two, or all three name formats.",style=MaterialTheme.typography.bodySmall);OutlinedTextField(category,{category=it},label={Text("Category")});OutlinedTextField(desc,{desc=it},label={Text("Description")});OutlinedTextField(barcode,{barcode=it},label={Text("Barcode (optional)")});OutlinedTextField(unit,{unit=it},label={Text("Default unit")});if(ocr.isNotBlank())Text("OCR: ${ocr.take(220)}",style=MaterialTheme.typography.bodySmall)
 }},confirmButton={TextButton(onClick={scope.launch{
  if(nameEn.isBlank()&&nameHi.isBlank()&&nameHinglish.isBlank())notice="Enter at least one product name: English, Hindi or Hinglish." else if(category.isBlank())notice="Category is required." else{
   val mergedAliases=(nameHinglish.split(",").map{it.trim()}+(editCatalog?.aliases?.split(",")?:emptyList()).map{it.trim()}).filter{it.isNotBlank()}.distinct().joinToString(", ")
   val item=CatalogProduct(id=editCatalog?.id?:0,nameEn=nameEn.trim(),nameHi=nameHi.trim(),category=category.trim(),description=desc.trim(),brand=brand.trim(),defaultUnit=unit.trim().ifBlank{"Piece"},barcode=barcode.trim(),aliases=mergedAliases)
   if(editCatalog==null){val id=dao.insertCatalogOne(item);activeShop?.let{dao.addShopProduct(ShopProduct(shopId=it.id,catalogId=id))}}
   else dao.updateCatalog(item)
   catalog=dao.getCatalog();refreshProducts();showProductForm=false;notice="Product saved."
  }
 }}){Text("Save")}},dismissButton={TextButton(onClick={showProductForm=false}){Text("Cancel")}})
 // Price form
 if(showPriceForm&&selected!=null)AlertDialog(onDismissRequest={showPriceForm=false},title={Text(if(editPrice==null)"Add price option" else "Edit price option")},text={Column{
  OutlinedTextField(qty,{qty=it},label={Text("Pack quantity")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal));OutlinedTextField(priceUnit,{priceUnit=it},label={Text("Unit (kg, g, L, ml, Piece, Packet, Bag...)")})
  OutlinedTextField(buy,{buy=it},label={Text("Purchase price ₹ (optional, private)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal));OutlinedTextField(sell,{sell=it},label={Text("Selling price ₹ (required)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
  val liveBuy=buy.toDoubleOrNull();val liveSell=sell.toDoubleOrNull()
  if(liveBuy!=null&&liveBuy>0&&liveSell!=null&&liveSell>=0){val d=liveSell-liveBuy;val pc=d/liveBuy*100.0;val mg=if(liveSell>0)d/liveSell*100.0 else 0.0;Text("${if(d>=0)"Profit" else "Loss"}: ${money(kotlin.math.abs(rupeesToPaise(d.toString())))}",color=if(d>=0)mint else Color.Red,fontWeight=FontWeight.Bold);Text("${if(d>=0)"Profit" else "Loss"} on cost: %.2f%% · Margin: %.2f%%".format(Locale.US,kotlin.math.abs(pc),kotlin.math.abs(mg)),style=MaterialTheme.typography.bodySmall)}else Text("Enter both prices to preview profit or loss and margin.",style=MaterialTheme.typography.bodySmall)
 }},confirmButton={TextButton(onClick={scope.launch{
  val q=qty.toDoubleOrNull();val sp=selected
  if(q==null||q<=0||priceUnit.isBlank()||sell.toDoubleOrNull()==null||sell.toDoubleOrNull()!!<0||(buy.isNotBlank()&&(buy.toDoubleOrNull()==null||buy.toDoubleOrNull()!!<0)))notice="Enter a valid quantity and selling price. Purchase price is optional."
  else if(sp!=null){
   val bp=if(buy.isBlank())0L else rupeesToPaise(buy);val se=rupeesToPaise(sell);val old=editPrice
   if(old==null){val id=dao.addPrice(PriceOption(shopProductId=sp.id,quantity=q,unit=priceUnit,purchasePaise=bp,sellingPaise=se));dao.addPriceHistory(PriceHistory(priceOptionId=id,purchasePaise=bp,sellingPaise=se))}
   else{dao.addPriceHistory(PriceHistory(priceOptionId=old.id,purchasePaise=old.purchasePaise,sellingPaise=old.sellingPaise));dao.updatePrice(old.copy(quantity=q,unit=priceUnit,purchasePaise=bp,sellingPaise=se,updatedAt=System.currentTimeMillis()));dao.addPriceHistory(PriceHistory(priceOptionId=old.id,purchasePaise=bp,sellingPaise=se))}
   refreshDetail();showPriceForm=false;notice="Price saved."
  }
 }}){Text("Save")}},dismissButton={TextButton(onClick={showPriceForm=false}){Text("Cancel")}})
 // Stock movement
 if(showStockForm&&selected!=null)AlertDialog(onDismissRequest={showStockForm=false},title={Text("Stock movement")},text={Column{
  Row{listOf("IN","OUT","ADJUST").forEach{FilterChip(selected=movement==it,onClick={movement=it},label={Text(it)})}}
  OutlinedTextField(stockQty,{stockQty=it},label={Text("Quantity")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal));OutlinedTextField(stockUnit,{stockUnit=it},label={Text("Unit")});OutlinedTextField(stockMin,{stockMin=it},label={Text("Low stock threshold")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal));OutlinedTextField(movementNote,{movementNote=it},label={Text("Note")})
 }},confirmButton={TextButton(onClick={scope.launch{
  val sp=selected;val amount=stockQty.toDoubleOrNull()
  if(sp==null||amount==null||amount<=0)notice="Enter a valid quantity." else{
   val old=dao.getBalance(sp.id);val next=when(movement){"IN"->(old?.quantity?:0.0)+amount;"OUT"->(old?.quantity?:0.0)-amount;else->amount}
   if(next<0)notice="Insufficient stock." else{
    dao.addMovement(StockMovement(shopProductId=sp.id,type=movement,quantity=amount,unit=stockUnit,note=movementNote))
    dao.saveBalance(StockBalance(id=old?.id?:0,shopProductId=sp.id,quantity=next,unit=stockUnit,minimum=stockMin.toDoubleOrNull()?:0.0))
    refreshDetail();showStockForm=false;notice=if(next<=(stockMin.toDoubleOrNull()?:0.0))"Stock saved. Low stock alert." else "Stock updated."
   }
  }
 }}){Text("Save")}},dismissButton={TextButton(onClick={showStockForm=false}){Text("Cancel")}})
 // Sale record
 if(showSaleForm&&selected!=null)AlertDialog(onDismissRequest={showSaleForm=false},title={Text("Record sale")},text={Column{
  OutlinedTextField(saleQty,{saleQty=it},label={Text("Quantity sold")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal));OutlinedTextField(saleNote,{saleNote=it},label={Text("Sale note")})
 }},confirmButton={TextButton(onClick={scope.launch{
  val sp=selected;val q=saleQty.toDoubleOrNull()
  if(sp==null||q==null||q<=0)notice="Enter a valid sale quantity." else{
   val bal=dao.getBalance(sp.id);val first=dao.getPrices(sp.id).firstOrNull()
   if(first==null)notice="Add a selling price first."
   else if(bal==null||bal.quantity<q)notice="Insufficient stock. Record stock-in first."
   else{
    val total=(first.sellingPaise*q).toLong();val cost=(first.purchasePaise*q).toLong()
    val saleId=dao.addSale(Sale(shopId=sp.shopId,totalPaise=total,totalCostPaise=cost,note=saleNote))
    val c=dao.getCatalog().firstOrNull{it.id==sp.catalogId}
    dao.addSaleItems(listOf(SaleItem(saleId=saleId,shopProductId=sp.id,productName=c?.nameEn?:"Product",quantity=q,unit=first.unit,costPaise=first.purchasePaise,sellingPaise=first.sellingPaise)))
    dao.addMovement(StockMovement(shopProductId=sp.id,type="SALE",quantity=q,unit=first.unit,unitCostPaise=first.purchasePaise,note="Sale #$saleId"))
    dao.saveBalance(bal.copy(quantity=bal.quantity-q,updatedAt=System.currentTimeMillis()))
    refreshDetail();sales=dao.getSales(sp.shopId);showSaleForm=false;notice="Sale recorded and stock reduced."
   }
  }
 }}){Text("Record sale")}},dismissButton={TextButton(onClick={showSaleForm=false}){Text("Cancel")}})
 // PIN dialog
 if(showPin)AlertDialog(onDismissRequest={showPin=false},title={Text(if(setupPin)"Set owner PIN" else "Owner login")},text={Column{
  Text(if(setupPin)"Create a 4 to 6 digit PIN." else "Enter your owner PIN.")
  OutlinedTextField(pinText,{pinText=it.filter(Char::isDigit).take(6)},label={Text("PIN")},visualTransformation=PasswordVisualTransformation(),keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.NumberPassword))
  if(setupPin)OutlinedTextField(pinAgain,{pinAgain=it.filter(Char::isDigit).take(6)},label={Text("Confirm PIN")},visualTransformation=PasswordVisualTransformation(),keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.NumberPassword))
  if(pinError.isNotBlank())Text(pinError,color=MaterialTheme.colorScheme.error)
 }},confirmButton={TextButton(onClick={
  if(setupPin){if(pinText.length !in 4..6||pinText!=pinAgain)pinError="PINs must match and be 4–6 digits." else{pin.set(pinText);owner=true;showPin=false;pinError=""}}
  else if(pin.verify(pinText)){owner=true;showPin=false;pinError=""}else pinError="Incorrect PIN."
 }){Text(if(setupPin)"Save PIN" else "Unlock")}},dismissButton={TextButton(onClick={showPin=false;pinError=""}){Text("Cancel")}})
}
}

@Composable
private fun ProductTile(sp:ShopProduct,c:CatalogProduct,dao:StoreDao,owner:Boolean,onClick:()->Unit){
 var price by remember{mutableStateOf<PriceOption?>(null)};var stock by remember{mutableStateOf<StockBalance?>(null)}
 LaunchedEffect(sp.id){price=dao.getPrices(sp.id).firstOrNull();stock=dao.getBalance(sp.id)}
 Card(Modifier.fillMaxWidth().clickable(onClick=onClick)){Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF3B3052)),contentAlignment=Alignment.Center){Icon(Icons.Default.Inventory2,null,tint=Color(0xFFBCA6FF))}
  Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){
   Text(listOf((if(sp.displayNameEn.isBlank())c.nameEn else sp.displayNameEn),(if(sp.displayNameHi.isBlank())c.nameHi else sp.displayNameHi),c.aliases).filter{it.isNotBlank()}.joinToString(" • "),fontWeight=FontWeight.Bold)
   Text(listOf(c.brand,c.category).filter{it.isNotBlank()}.joinToString(" · "),style=MaterialTheme.typography.labelSmall)
   Text(price?.let{"${it.quantity} ${it.unit} · ${money(it.sellingPaise)}"}?:"Price not set",color=MaterialTheme.colorScheme.primary)
   stock?.let{Text("Stock: ${it.quantity} ${it.unit}${if(it.quantity<=it.minimum)" · LOW" else ""}",color=if(it.quantity<=it.minimum)Color(0xFFFFB86B) else mint,style=MaterialTheme.typography.labelSmall)}
  };Icon(Icons.Default.ChevronRight,null)
 }}
}

private fun calculateExpression(raw:String):Double?{
 val s=raw.replace(" ","");if(s.isBlank()||!s.matches(Regex("[0-9.+*/()\\-]+")))return null
 // Shunting-yard evaluator, including parentheses and unary minus.
 return try{
  val nums=mutableListOf<Double>();val ops=mutableListOf<Char>();var i=0
  fun prec(c:Char)=if(c=='*'||c=='/')2 else 1
  fun apply(){val op=ops.removeAt(ops.lastIndex);val b=nums.removeAt(nums.lastIndex);val a=nums.removeAt(nums.lastIndex);nums.add(when(op){'+'->a+b;'-'->a-b;'*'->a*b;'/'->a/b;else->error("op")})}
  while(i<s.length){
   val c=s[i]
   if(c.isDigit()||c=='.'||(c=='-'&&(i==0||s[i-1] in "+-*/("))){
    val start=i;i++;while(i<s.length&&(s[i].isDigit()||s[i]=='.'))i++
    val n=s.substring(start,i).toDouble();nums.add(n)
   }else if(c=='('){ops.add(c);i++}
   else if(c==')'){while(ops.isNotEmpty()&&ops.last()!='(')apply();if(ops.isEmpty())return null;ops.removeAt(ops.lastIndex);i++}
   else if(c in "+-*/"){while(ops.isNotEmpty()&&ops.last()!='('&&prec(ops.last())>=prec(c))apply();ops.add(c);i++}
   else return null
  }
  while(ops.isNotEmpty()){if(ops.last()=='(')return null;apply()}
  nums.singleOrNull()?.takeIf{it.isFinite()}
 }catch(_:Exception){null}
}
