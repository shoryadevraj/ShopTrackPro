package com.shoptrackpro.app
import android.content.Context
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PinManager(c:Context) {
 private val prefs=c.getSharedPreferences("shoptrack_owner",Context.MODE_PRIVATE)
 fun exists()=prefs.contains("hash") && prefs.contains("salt")
 fun set(pin:String) {
  require(pin.length in 4..6 && pin.all{it.isDigit()})
  val salt=ByteArray(16).also{SecureRandom().nextBytes(it)}
  prefs.edit().putString("salt",salt.hex()).putString("hash",digest(pin,salt)).apply()
 }
 fun verify(pin:String):Boolean {
  val salt=prefs.getString("salt",null)?.bytes()?:return false
  val hash=prefs.getString("hash",null)?:return false
  return MessageDigest.isEqual(hash.toByteArray(),digest(pin,salt).toByteArray())
 }
 private fun digest(pin:String,salt:ByteArray):String {
  val spec=PBEKeySpec(pin.toCharArray(),salt,150000,256)
  return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded.hex()
 }
 private fun ByteArray.hex()=joinToString(""){"%02x".format(it)}
 private fun String.bytes()=chunked(2).map{it.toInt(16).toByte()}.toByteArray()
}
