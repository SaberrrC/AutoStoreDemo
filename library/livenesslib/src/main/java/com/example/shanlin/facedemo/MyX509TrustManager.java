package com.example.shanlin.facedemo;

import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class MyX509TrustManager implements X509TrustManager{
    private static final String TAG = MyX509TrustManager.class.getSimpleName();
	/*
	 * The default X509TrustManager returned by SunX509. We’ll delegate
	 * decisions to it, and fall back to the logic in this class if the default
	 * X509TrustManager doesn’t trust it.
	 */
	X509TrustManager sunJSSEX509TrustManager;

	public MyX509TrustManager(InputStream ins) throws Exception {
		// create a “default” JSSE X509TrustManager.

		CertificateFactory cerFactory = CertificateFactory
				.getInstance("X.509");
		java.security.cert.Certificate cer = cerFactory.generateCertificate(ins);
//		KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
		 KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		 keyStore.load(ins,"click-v0629".toCharArray());
		keyStore.load(null,null);
		 keyStore.setCertificateEntry("trust", cer);
//		((X509Certificate)cer).checkValidity();
//		ks.load(new FileInputStream("trustedCerts"), "passphrase".toCharArray());
		
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509",
//				"SunJSSE");
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

		trustManagerFactory.init(keyStore);

		TrustManager tms[] = trustManagerFactory.getTrustManagers();

//		SSLContext sslc = SSLContext.getInstance("SSLv3");
//		sslc.init(null,tms,null);

		/*
		 * Iterate over the returned trustmanagers, look for an instance of
		 * X509TrustManager. If found, use that as our “default” trust manager.
		 */
		for (int i = 0; i < tms.length; i++) {
			if (tms[i] instanceof X509TrustManager) {
				sunJSSEX509TrustManager = (X509TrustManager) tms[i];
				return;
			}
		}

		/*
		 * Find some other way to initialize, or else we have to fail the
		 * constructor.
		 */
		throw new Exception("Couldn’t initialize");
	}

	/*
	 * Delegate to the default trust manager.
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		Log.d(TAG,"checkClientTrusted");
//		try {
			sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
//		} catch (CertificateException excep) {
//			// do any special handling here, or rethrow exception.
//		}
	}

	/*
	 * Delegate to the default trust manager.
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		Log.d(TAG,"checkServerTrusted");
//		try {
			sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
//		} catch (CertificateException excep) {
//			/*
//			 * Possibly pop up a dialog box asking whether to trust the cert
//			 * chain.
//			 */
//		}
	}

	/*
	 * Merely pass this through.
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return sunJSSEX509TrustManager.getAcceptedIssuers();
	}
}

// // 创建SSLContext对象，并使用我们指定的信任管理器初始化
// TrustManager[] tm = { new MyX509TrustManager() };
// SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//
// sslContext.init(null, tm, new java.security.SecureRandom());
//
// // 从上述SSLContext对象中得到SSLSocketFactory对象
// SSLSocketFactory ssf = sslContext.getSocketFactory();
//
// // 创建URL对象
// URL myURL = new URL(“https://ebanks.gdb.com.cn/sperbank/perbankLogin.jsp”);
//
// // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
// HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
// httpsConn.setSSLSocketFactory(ssf);
//
// // 取得该连接的输入流，以读取响应内容
// InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
//
// // 读取服务器的响应内容并显示
// int respInt = insr.read();
// while (respInt != -1) {
// System.out.print((char) respInt);
// respInt = insr.read();
// }