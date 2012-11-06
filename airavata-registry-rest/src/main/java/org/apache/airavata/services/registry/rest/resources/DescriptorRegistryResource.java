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

package org.apache.airavata.services.registry.rest.resources;

import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.registry.api.AiravataRegistry2;
import org.apache.airavata.registry.api.exception.RegistryException;
import org.apache.airavata.registry.api.exception.gateway.DescriptorAlreadyExistsException;
import org.apache.airavata.registry.api.exception.gateway.DescriptorDoesNotExistsException;
import org.apache.airavata.registry.api.exception.gateway.MalformedDescriptorException;
import org.apache.airavata.services.registry.rest.resourcemappings.*;
import org.apache.airavata.services.registry.rest.utils.DescriptorUtil;
import org.apache.airavata.services.registry.rest.utils.RestServicesConstants;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/registry/api/descriptors")
public class DescriptorRegistryResource {
    private AiravataRegistry2 airavataRegistry;

    @Context
    ServletContext context;

    /**
     * ---------------------------------Descriptor Registry----------------------------------*
     */


    @GET
    @Path("hostdescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isHostDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isHostDescriptorExists(descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addJSONHostDescriptor(HostDescriptor host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = DescriptorUtil.createHostDescription(host);
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateHostDescriptor(HostDescriptor host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = DescriptorUtil.createHostDescription(host);
            airavataRegistry.updateHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("host/description")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = airavataRegistry.getHostDescriptor(hostName);
            HostDescriptor hostDescriptor = DescriptorUtil.createHostDescriptor(hostDescription);
            if (hostDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(hostDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    @DELETE
    @Path("hostdescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeHostDescriptor(hostName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/hostdescriptors")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<HostDescription> hostDescriptionList = airavataRegistry.getHostDescriptors();
            HostDescriptionList list = new HostDescriptionList();
            HostDescriptor[] hostDescriptions = new HostDescriptor[hostDescriptionList.size()];
            for (int i = 0; i < hostDescriptionList.size(); i++) {
                HostDescriptor hostDescriptor = DescriptorUtil.createHostDescriptor(hostDescriptionList.get(i));
                hostDescriptions[i] = hostDescriptor;
            }
            list.setHostDescriptions(hostDescriptions);
            if (hostDescriptionList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("servicedescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isServiceDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isServiceDescriptorExists(descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("servicedescriptor/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addJSONServiceDescriptor(ServiceDescriptor service) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(service);
            airavataRegistry.addServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("servicedescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateServiceDescriptor(ServiceDescriptor service) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(service);
            airavataRegistry.updateServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("servicedescriptor/description")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptor(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            if (serviceDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(serviceDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("servicedescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeServiceDescriptor(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeServiceDescriptor(serviceName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/servicedescriptors")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<ServiceDescription> serviceDescriptors = airavataRegistry.getServiceDescriptors();
            ServiceDescriptionList list = new ServiceDescriptionList();
            ServiceDescriptor[] serviceDescriptions = new ServiceDescriptor[serviceDescriptors.size()];
            for (int i = 0; i < serviceDescriptors.size(); i++) {
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescriptors.get(i));
                serviceDescriptions[i] = serviceDescriptor;
            }
            list.setServiceDescriptions(serviceDescriptions);
            if (serviceDescriptors.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("applicationdescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isApplicationDescriptorExists(@QueryParam("serviceName") String serviceName,
                                                  @QueryParam("hostName") String hostName,
                                                  @QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isApplicationDescriptorExists(serviceName, hostName, descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @POST
    @Path("applicationdescriptor/build/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addJSONApplicationDescriptor(ApplicationDescriptor applicationDescriptor) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String hostdescName = applicationDescriptor.getHostdescName();
            if (!airavataRegistry.isHostDescriptorExists(hostdescName)) {
                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return builder.build();
            }
            ApplicationDeploymentDescription applicationDeploymentDescription = DescriptorUtil.createApplicationDescription(applicationDescriptor);
            ServiceDescriptor serviceDescriptor = applicationDescriptor.getServiceDescriptor();
            String serviceName;
            if (serviceDescriptor != null) {
                if (serviceDescriptor.getServiceName() == null) {
                    serviceName = applicationDescriptor.getName();
                    serviceDescriptor.setServiceName(serviceName);
                } else {
                    serviceName = serviceDescriptor.getServiceName();
                }
                ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(serviceDescriptor);
                if (!airavataRegistry.isServiceDescriptorExists(serviceName)) {
                    airavataRegistry.addServiceDescriptor(serviceDescription);
                }
            } else {
                serviceName = applicationDescriptor.getName();
            }
            airavataRegistry.addApplicationDescriptor(serviceName, hostdescName, applicationDeploymentDescription);


            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @POST
    @Path("applicationdescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response udpateApplicationDescriptorByDescriptors(ApplicationDescriptor applicationDescriptor) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String hostdescName = applicationDescriptor.getHostdescName();
            if (!airavataRegistry.isHostDescriptorExists(hostdescName)) {
                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return builder.build();
            }
            ApplicationDeploymentDescription applicationDeploymentDescription = DescriptorUtil.createApplicationDescription(applicationDescriptor);
            ServiceDescriptor serviceDescriptor = applicationDescriptor.getServiceDescriptor();
            String serviceName;
            if (serviceDescriptor != null) {
                if (serviceDescriptor.getServiceName() == null) {
                    serviceName = applicationDescriptor.getName();
                    serviceDescriptor.setServiceName(serviceName);
                } else {
                    serviceName = serviceDescriptor.getServiceName();
                }
                ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(serviceDescriptor);
                if (airavataRegistry.isServiceDescriptorExists(serviceName)) {
                    airavataRegistry.updateServiceDescriptor(serviceDescription);
                } else {
                    airavataRegistry.addServiceDescriptor(serviceDescription);
                }

            } else {
                serviceName = applicationDescriptor.getName();
            }
            airavataRegistry.updateApplicationDescriptor(serviceName, hostdescName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("applicationdescriptor/description")
    @Produces("text/xml")
    public Response getApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                             @QueryParam("hostName") String hostName,
                                             @QueryParam("applicationName") String applicationName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ApplicationDeploymentDescription applicationDeploymentDescription = airavataRegistry.getApplicationDescriptor(serviceName, hostName, applicationName);
            ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
            applicationDescriptor.setHostdescName(hostName);
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            applicationDescriptor.setServiceDescriptor(serviceDescriptor);

            if (applicationDeploymentDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptors/alldescriptors/host/service")
    @Produces("text/xml")
    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName,
                                              @QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ApplicationDeploymentDescription applicationDeploymentDescription = airavataRegistry.getApplicationDescriptors(serviceName, hostName);
            ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
            applicationDescriptor.setHostdescName(hostName);
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            applicationDescriptor.setServiceDescriptor(serviceDescriptor);

            if (applicationDeploymentDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptor/alldescriptors/service")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Map<String, ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors(serviceName);
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
            int i = 0;
            for (String hostName : applicationDeploymentDescriptionMap.keySet()) {
                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(hostName);
                ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
                applicationDescriptor.setHostdescName(hostName);

                ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
                applicationDescriptor.setServiceDescriptor(serviceDescriptor);

                applicationDescriptors[i] = applicationDescriptor;
                i++;
            }
            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
            if (applicationDeploymentDescriptionMap.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptorList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (MalformedDescriptorException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptor/alldescriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Map<String[], ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors();
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
            int i = 0;
            for (String[] descriptors : applicationDeploymentDescriptionMap.keySet()) {
                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(descriptors);
                ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
                applicationDescriptor.setHostdescName(descriptors[1]);
                ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(descriptors[0]);
                if (serviceDescription == null) {
                    Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                    return builder.build();
                }
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
                applicationDescriptor.setServiceDescriptor(serviceDescriptor);
                applicationDescriptors[i] = applicationDescriptor;
                i++;
            }
            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
            if (applicationDeploymentDescriptionMap.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptorList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (MalformedDescriptorException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("applicationdescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                                @QueryParam("hostName") String hostName,
                                                @QueryParam("appName") String appName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeApplicationDescriptor(serviceName, hostName, appName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }
}
