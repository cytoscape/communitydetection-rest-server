package org.ndexbio.communitydetection.rest; 


import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.ndexbio.communitydetection.rest.services.CommunityDetection;
import org.ndexbio.communitydetection.rest.services.Configuration;
import org.ndexbio.communitydetection.rest.services.Diffusion;
import org.ndexbio.communitydetection.rest.services.Status;

public class CommunityDetectionApplication extends Application {

    private final Set<Object> _singletons = new HashSet<Object>();
    public CommunityDetectionApplication() {        
        // Register our hello service
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowCredentials(true);
        _singletons.add(corsFilter);
    }
    @Override
    public Set<Object> getSingletons() {
        return _singletons;
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        // @TODO add logic to ony add Diffusion.class if there is a diffusion
        //       algorithm present
        return Stream.of(CommunityDetection.class,
			 Diffusion.class,
                         Status.class).collect(Collectors.toSet());
    }
}