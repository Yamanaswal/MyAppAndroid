package com.yaman.myapplication.aes

import android.util.Base64
import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object AES {

    private const val pswdIterations = 10
    private const val keySize = 128
    private const val cypherInstance = "AES/CBC/PKCS5Padding"
    private const val secretKeyInstance = "PBKDF2WithHmacSHA1"
    private const val passwordText = "sampleText"
    private const val AESSalt = "exampleSalt"
    private const val initializationVector = "8119745113154120"


    fun encrypt(textToEncrypt: String): String {
        val skeySpec = SecretKeySpec(getRaw(), "AES")
        val cipher = Cipher.getInstance(cypherInstance)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            skeySpec,
            IvParameterSpec(initializationVector.toByteArray())
        )
        val encrypted = cipher.doFinal(textToEncrypt.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }


    fun decrypt(textToDecrypt: String?): String {
        val encryted_bytes: ByteArray = Base64.decode(textToDecrypt, Base64.DEFAULT)
        val skeySpec = SecretKeySpec(getRaw(), "AES")
        val cipher = Cipher.getInstance(cypherInstance)
        cipher.init(
            Cipher.DECRYPT_MODE,
            skeySpec,
            IvParameterSpec(initializationVector.toByteArray())
        )
        val decrypted = cipher.doFinal(encryted_bytes)
        return String(decrypted, Charset.forName("UTF-8"))
    }

    private fun getRaw(): ByteArray {
        try {
            val factory: SecretKeyFactory = SecretKeyFactory.getInstance(secretKeyInstance)
            val spec: KeySpec =
                PBEKeySpec(passwordText.toCharArray(), AESSalt.toByteArray(), pswdIterations, keySize)
            return factory.generateSecret(spec).encoded
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }
}