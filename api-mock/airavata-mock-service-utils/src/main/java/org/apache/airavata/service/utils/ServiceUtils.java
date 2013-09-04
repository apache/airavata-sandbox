package org.apache.airavata.service.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.airavata.service.utils.model.DataList;

public class ServiceUtils {
	public static DataList WrapList(List<String> list){
		DataList dataList = new DataList();
		dataList.setList(list);
		return dataList;
	}
	
	public static URI getServiceOperationURIFromHelpURI(UriInfo uriInfo) throws URISyntaxException {
		String p = uriInfo.getAbsolutePath().getPath();
		if (p.endsWith("/")){
			p=p.substring(0,p.length()-1);
		}
		if (p.startsWith("/")){
			p=p.substring(1);
		}
		String[] pathSegments = p.split("/");
		String path="";
		for (int i = 0; i < pathSegments.length-1; i++) {
			path+="/"+pathSegments[i];
		}
		URI u = uriInfo.getBaseUri();
		URI uri = new URI(u.getScheme(),u.getUserInfo(),u.getHost(),u.getPort(),path,null,null);
		return uri;
	}
	
}
