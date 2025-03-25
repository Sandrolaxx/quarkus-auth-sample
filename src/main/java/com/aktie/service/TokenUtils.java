package com.aktie.service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;

/**
 * @author SRamos
 */
public class TokenUtils {

    public static String generateTokenString(JwtClaims claims) throws Exception {
        PrivateKey pk = readPrivateKey("/privateKey.pem");

        return generateTokenString(pk, "/privateKey.pem", claims);
    }

    private static String generateTokenString(PrivateKey privateKey, String kid, JwtClaims claims) throws Exception {

        long currentTimeInSecs = (int) (System.currentTimeMillis() / 1000);

        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));
        claims.setClaim(Claims.auth_time.name(), NumericDate.fromSeconds(currentTimeInSecs));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue(kid);
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    public static PrivateKey readPrivateKey(final String pemResName) throws Exception {

        InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName);

        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);

        return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
    }

    public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);

        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);

        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");

        return pem.trim();
    }

}
