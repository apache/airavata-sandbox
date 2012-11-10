package org.apache.airavata.jobsubmission.gram;

import java.net.URI;

import org.apache.airavata.jobsubmission.context.ApplicationContext;
import org.apache.airavata.jobsubmission.utils.GridFtp;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ietf.jgss.GSSCredential;

public class FileTransfer {

	public void makeDir(GSSCredential gssCred, URI destURI) throws Exception {
		GridFtp ftp = new GridFtp();
		ftp.makeDir(destURI, gssCred);
	}

	public void transferData(GSSCredential gssCred, URI srcURI, URI destURI) throws Exception {
		GridFtp ftp = new GridFtp();
		ftp.transfer(srcURI, destURI, gssCred, true);
	}

	public void transferData(GSSCredential gssCred, String sourceERP, String remoteSrcFile, String targetERP,
			String remoteDestFile) throws Exception {
		GridFtp ftp = new GridFtp();
		URI srcURI = GridFtp.createGsiftpURI(sourceERP, remoteSrcFile);
		URI destURI = GridFtp.createGsiftpURI(targetERP, remoteDestFile);
		ftp.transfer(srcURI, destURI, gssCred, true);
	}

	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			Logger logger = Logger.getLogger("GridFTPClient");
			Level lev = Level.toLevel("DEBUG");
			logger.setLevel(lev);
			ApplicationContext context = new ApplicationContext();
			context.login();
			ExectionContext contextExectionContext = new ExectionContext();
			FileTransfer fileTransfer = new FileTransfer();

			String sourceERP = contextExectionContext.getGridFTPServerSource();
			String remoteSrcFile = contextExectionContext.getSourcedataLocation();
			String targeterp = contextExectionContext.getGridFTPServerDest();
			String remoteDestFile = contextExectionContext.getDestdataLocation();
			URI srcURI = GridFtp.createGsiftpURI(sourceERP, remoteSrcFile);
			URI destURI = GridFtp.createGsiftpURI(targeterp, remoteDestFile);
			URI dirLocation = GridFtp.createGsiftpURI(targeterp,
					remoteDestFile.substring(0, remoteDestFile.lastIndexOf("/")));
			GSSCredential gssCredential = context.getGssCredential();
			System.out.println(dirLocation);
			fileTransfer.makeDir(gssCredential, dirLocation);
			fileTransfer.transferData(gssCredential, srcURI, destURI);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}