package edu.bethlehem.post_service.Proxies;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "opinion-service")
@EnableDiscoveryClient
public interface OpinionServiceProxy {

    @DeleteMapping("/opinions/{id}")
    public ResponseEntity<?> deleteOpinion(@PathVariable Long id,
            @RequestHeader(name = "Authorization") String jwtToken);

}
