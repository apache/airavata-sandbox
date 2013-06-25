/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.filetransfer;

import junit.framework.Assert;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.x500.*;
import junit.framework.TestCase;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import org.globus.gsi.SigningPolicy;
import org.globus.gsi.SigningPolicyParser;
import org.globus.gsi.util.CertificateIOUtil;
import org.globus.util.GlobusResource;
import org.junit.Ignore;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/7/13
 * Time: 9:57 AM
 */

@Ignore("This test case used to debug JGlobus-102. No need to run this test with other gridftp tests.")
public class CertFileReadTest extends TestCase {

    private static MessageDigest md5;

    private static String CERT_FILE_LOCATION = "/Users/thejaka/development/apache/airavata/sandbox/grid-tools/gridftp-client/certificates/";

    public void testCertFileRead() throws Exception {

        String path1 = CERT_FILE_LOCATION + "ffc3d59b";
        String path2 = CERT_FILE_LOCATION + "e5cc84c2";


        GlobusResource globusResource1 = new GlobusResource(path1 + ".signing_policy");
        GlobusResource globusResource2 = new GlobusResource(path2 + ".signing_policy");
        //GlobusResource globusResource3 = new GlobusResource("/Users/thejaka/development/apache/airavata/sandbox/grid-tools/gridftp-client/certificates/ef300431.signing_policy");
        //GlobusResource globusResource4 = new GlobusResource("/Users/thejaka/development/apache/airavata/sandbox/grid-tools/gridftp-client/certificates/01b5d333.signing_policy");
        //GlobusResource globusResource5 = new GlobusResource("/Users/thejaka/development/apache/airavata/sandbox/grid-tools/gridftp-client/certificates/081fefd0.signing_policy");
        //ResourceSigningPolicy resourceSigningPolicy = new ResourceSigningPolicy(globusResource);

        // ===== Testing globusResource1 - This should pass (cos no DC components) ================ //
        X509Certificate crt1 = readCertificate(path1 + ".0");
        X500Principal policySubjectCert1 = getPrincipal(globusResource1);

        String certHash1 = CertificateIOUtil.nameHash(crt1.getSubjectX500Principal());
        String principalHash1 = CertificateIOUtil.nameHash(policySubjectCert1);

        System.out.println("======== Printing hashes for 1 ================");
        System.out.println(certHash1);
        System.out.println(principalHash1);

        Assert.assertEquals("Certificate hash value does not match with the hash value generated using principal name.",
                certHash1, principalHash1);

        // ===== Testing globusResource1 - This should fail (cos we have DC components) ================ //
        X509Certificate crt2 = readCertificate(path2 + ".0");
        X500Principal policySubjectCert2 = getPrincipal(globusResource2);

        String certHash2 = CertificateIOUtil.nameHash(crt2.getSubjectX500Principal());
        String principalHash2 = CertificateIOUtil.nameHash(policySubjectCert2);

        System.out.println("======== Printing hashes for 2 ================");
        System.out.println(certHash2);
        System.out.println(principalHash2);

        Assert.assertEquals("Certificate hash value does not match with the hash value generated using principal name.",
                certHash2, principalHash2);

        //X509Certificate crt = readCertificate(path1 + ".0");
        //X509Certificate crt2 = readCertificate(path2 + ".0");

        //System.out.println("=======================================");
        //System.out.println(crt.getIssuerX500Principal().getName());




        //X500Principal certPrincipal = crt.getSubjectX500Principal();

        //X500Principal policySubjectCert = getPrincipal(globusResource1);
        //"CN=TACC Classic CA,O=UT-AUSTIN,DC=TACC,DC=UTEXAS,DC=EDU"
        //X500Principal policySubjectCert = new X500Principal(certPrincipal.getName());

        //System.out.println(CertificateIOUtil.nameHash(certPrincipal));
        //System.out.println(CertificateIOUtil.nameHash((policySubjectCert)));




        //ByteArrayOutputStream bout = new ByteArrayOutputStream();
        //DEROutputStream der = new DEROutputStream(bout);
        //der.writeObject(name.getDERObject());







        //====================
        //X500Principal certPrincipal2 = crt2.getSubjectX500Principal();
        // X500Principal policySubjectCert = getPrincipal(globusResource1);
        //"CN=TACC Classic CA,O=UT-AUSTIN,DC=TACC,DC=UTEXAS,DC=EDU"
        //X500Principal policySubjectCert2 = new X500Principal(certPrincipal2.getName());

        //System.out.println(CertificateIOUtil.nameHash(certPrincipal2));
        //System.out.println(CertificateIOUtil.nameHash((policySubjectCert2)));

        //Assert.assertEquals(getHash(globusResource1), "e5cc84c2");
        //Assert.assertEquals(getHash(globusResource2), "ffc3d59b");
        //Assert.assertEquals(getHash(globusResource3), "ef300431");
        //Assert.assertEquals(getHash(globusResource4), "01b5d333");
        //Assert.assertEquals(getHash(globusResource5), "081fefd0");

    }

    private String getHash(GlobusResource globusResource) throws Exception {

        X500Principal principal = getPrincipal(globusResource);

        System.out.println(principal.getName());

        return CertificateIOUtil.nameHash(principal);

    }

    private X500Principal getPrincipal(GlobusResource globusResource) throws Exception{

        SigningPolicyParser parser = new SigningPolicyParser();

        Reader reader = new InputStreamReader(globusResource.getInputStream());

        Map<X500Principal, SigningPolicy> policies = parser.parse(reader);

        return policies.keySet().iterator().next();

    }

    private X509Certificate readCertificate(String certPath) {
        try {
            FileInputStream fr = new FileInputStream(certPath);
            CertificateFactory cf =
                    CertificateFactory.getInstance("X509");
            X509Certificate crt = (X509Certificate)
                    cf.generateCertificate(fr);
            System.out.println("Read certificate:");
            System.out.println("\tCertificate for: " +
                    crt.getSubjectDN());
            System.out.println("\tCertificate issued by: " +
                    crt.getIssuerDN());
            System.out.println("\tCertificate is valid from " +
                    crt.getNotBefore() + " to " + crt.getNotAfter());
            System.out.println("\tCertificate SN# " +
                    crt.getSerialNumber());
            System.out.println("\tGenerated with " +
                    crt.getSigAlgName());

            return crt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static synchronized String hash(byte[] data) {
        init();
        if (md5 == null) {
            return null;
        }

        md5.reset();
        md5.update(data);

        byte[] md = md5.digest();

        long ret = (fixByte(md[0]) | (fixByte(md[1]) << 8L));
        ret = ret | fixByte(md[2]) << 16L;
        ret = ret | fixByte(md[3]) << 24L;
        ret = ret & 0xffffffffL;

        return Long.toHexString(ret);
    }

    private static long fixByte(byte b) {
        return (b < 0) ? (long) (b + 256) : (long) b;
    }

    private static void init() {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

}
