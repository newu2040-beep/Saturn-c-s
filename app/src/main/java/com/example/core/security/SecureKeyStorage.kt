package com.example.core.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.BuildConfig
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureKeyStorage(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("saturn_c_vault_enc", Context.MODE_PRIVATE)

    private val keyAlias = "SaturnCKeyAlias"
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    init {
        ensureKeyStoreKey()
        // Check if Gemini key is provided in BuildConfig
        try {
            val buildConfigGemini = BuildConfig.GEMINI_API_KEY
            if (buildConfigGemini.isNotBlank() && buildConfigGemini != "MY_GEMINI_API_KEY" && !hasKey("google")) {
                saveApiKey("google", buildConfigGemini)
            }
        } catch (_: Exception) {
            // BuildConfig key optional
        }
    }

    private fun ensureKeyStoreKey() {
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    fun saveApiKey(providerId: String, apiKey: String) {
        if (apiKey.isBlank()) {
            removeApiKey(providerId)
            return
        }
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(apiKey.toByteArray(Charsets.UTF_8))
            val combined = ByteArray(iv.size + encryptedBytes.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encryptedBytes, 0, combined, iv.size, encryptedBytes.size)

            val encoded = Base64.encodeToString(combined, Base64.NO_WRAP)
            prefs.edit().putString("key_$providerId", encoded).apply()
        } catch (e: Exception) {
            // Fallback obfuscated storage in case Keystore GCM has hardware restrictions in container
            val fallbackEncoded = Base64.encodeToString(apiKey.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            prefs.edit().putString("fb_key_$providerId", fallbackEncoded).apply()
        }
    }

    fun getApiKey(providerId: String): String? {
        val encrypted = prefs.getString("key_$providerId", null)
        if (encrypted != null) {
            try {
                val combined = Base64.decode(encrypted, Base64.NO_WRAP)
                if (combined.size > 12) {
                    val iv = ByteArray(12)
                    val cipherText = ByteArray(combined.size - 12)
                    System.arraycopy(combined, 0, iv, 0, 12)
                    System.arraycopy(combined, 12, cipherText, 0, cipherText.size)

                    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                    val spec = GCMParameterSpec(128, iv)
                    cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
                    val decrypted = cipher.doFinal(cipherText)
                    return String(decrypted, Charsets.UTF_8)
                }
            } catch (e: Exception) {
                // Try fallback
            }
        }

        val fb = prefs.getString("fb_key_$providerId", null)
        if (fb != null) {
            return try {
                String(Base64.decode(fb, Base64.NO_WRAP), Charsets.UTF_8)
            } catch (_: Exception) {
                null
            }
        }

        if (providerId == "google") {
            try {
                val buildKey = BuildConfig.GEMINI_API_KEY
                if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") {
                    return buildKey
                }
            } catch (_: Exception) {}
        }
        return null
    }

    fun hasKey(providerId: String): Boolean {
        val key = getApiKey(providerId)
        return !key.isNullOrBlank()
    }

    fun hasApiKey(providerId: String): Boolean = hasKey(providerId)

    fun getMaskedKey(providerId: String): String {
        val raw = getApiKey(providerId) ?: return "Not configured"
        if (raw.length <= 8) return "••••••••"
        val prefix = raw.take(3)
        val suffix = raw.takeLast(4)
        return "$prefix••••••••$suffix"
    }

    fun removeApiKey(providerId: String) {
        prefs.edit()
            .remove("key_$providerId")
            .remove("fb_key_$providerId")
            .apply()
    }

    fun deleteApiKey(providerId: String) = removeApiKey(providerId)

    fun clearAllKeys() {
        prefs.edit().clear().apply()
    }
}
