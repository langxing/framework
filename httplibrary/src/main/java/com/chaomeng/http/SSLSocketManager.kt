package com.chaomeng.http

import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class SSLSocketManager {

    companion object {
        private val instance: SSLSocketManager by lazy {
            SSLSocketManager()
        }

        fun get() = instance
    }

    fun defaultSSLSocketFactory(isSelfSigned: Boolean = false): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val trustManager = defaultTrustManager(isSelfSigned)
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf(trustManager), null)
            ssfFactory = sc.socketFactory
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return ssfFactory
    }

    fun defaultTrustManager(isSelfSigned: Boolean = false, stream: InputStream? = null): X509TrustManager {
        return if (isSelfSigned) trustManagerForCertificates(stream) else TrustManager.instance
    }

    /**
     * 自签证书的trustManager
     *
     * @param in 自签证书输入流
     * @return 信任自签证书的trustManager
     * @throws GeneralSecurityException
     */
    @Throws(GeneralSecurityException::class)
    private fun trustManagerForCertificates(`in`: InputStream?): X509TrustManager {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        //通过证书工厂得到自签证书对象集合
        val certificates = certificateFactory.generateCertificates(`in`)
        if (certificates.isEmpty()) {
            throw IllegalArgumentException("expected non-empty set of trusted certificates")
        }
        //为证书设置一个keyStore
        val password = "password".toCharArray() // Any password will work.
        val keyStore = newEmptyKeyStore(password)
        //将证书放入keystore中
        for ((index, certificate) in certificates.withIndex()) {
            val certificateAlias = (index).toString()
            keyStore.setCertificateEntry(certificateAlias, certificate)
        }
        // Use it to build an X509 trust manager.
        //使用包含自签证书信息的keyStore去构建一个X509TrustManager
        val keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm()
        )
        keyManagerFactory.init(keyStore, password)
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(keyStore)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException(
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            )
        }
        return trustManagers[0] as X509TrustManager
    }

    @Throws(GeneralSecurityException::class)
    private fun newEmptyKeyStore(password: CharArray): KeyStore {
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, password)
            return keyStore
        } catch (e: IOException) {
            throw AssertionError(e)
        }

    }

    class TrustManager : X509TrustManager {

        companion object {
            val instance: TrustManager by lazy {
                TrustManager()
            }
        }

        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }

    }

    class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }


}