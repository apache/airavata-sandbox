/*
 * Copyright (c) 2009 Pervasive Lab, Indiana University. All rights reserved.
 *
 * This software is open source. See the bottom of this file for the license.
 *
 * $Id: $
 */
package org.apache.airavata.jobsubmission.context;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.globus.gsi.CertUtil;
import org.globus.util.ClassLoaderUtils;



/**
 * @author Raminderjeet Singh
 */
public class CertificateManager {

  
 
    private static X509Certificate[] trustedCertificates;

    /**
     * Load CA certificates from a file included in the XBaya jar.
     * 
     * @return The trusted certificates.
     */
    public static X509Certificate[] getTrustedCertificate(String certificate) {
        if (trustedCertificates != null) {
            return trustedCertificates;
        }

        List<X509Certificate> extremeTrustedCertificates = getTrustedCertificates(certificate);
 
        List<X509Certificate> allTrustedCertificates = new ArrayList<X509Certificate>();
        allTrustedCertificates.addAll(extremeTrustedCertificates);
 
        trustedCertificates = allTrustedCertificates
                .toArray(new X509Certificate[allTrustedCertificates.size()]);
        return trustedCertificates;
    }


    private static List<X509Certificate> getTrustedCertificates(String pass) {
    	//ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //**
    	//InputStream stream = classLoader.getResourceAsStream(pass); //**
    	InputStream stream = ClassLoaderUtils.getResourceAsStream(pass); //**
        if (stream == null) {
            throw new RuntimeException("Failed to get InputStream to "
                    + pass);
        }
        return readTrustedCertificates(stream);
    }
    
    /**
     * @param stream
     * @return List of X509Certificate
     */
    public static List<X509Certificate> readTrustedCertificates(
            InputStream stream) {
        ArrayList<X509Certificate> certificates = new ArrayList<X509Certificate>();
        while (true) {
            X509Certificate certificate;
            try {
            	 certificate = CertUtil.loadCertificate(stream);
//                certificate = CertificateLoadUtil.loadCertificate(stream); //**
            }catch (GeneralSecurityException e) {
                String message = "Certificates are invalid";
                throw new RuntimeException(message, e);
            }
            if (certificate == null) {
                break;
            }
            certificates.add(certificate);
        }
        return certificates;
    }

}

