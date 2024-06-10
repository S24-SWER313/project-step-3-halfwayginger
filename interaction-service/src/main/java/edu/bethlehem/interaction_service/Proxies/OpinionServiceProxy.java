package edu.bethlehem.interaction_service.Proxies;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "opinion-service")
@EnableDiscoveryClient
public interface OpinionServiceProxy {

    @GetMapping("/opinions/exi/{postId}")
    public Boolean existes(@PathVariable long postId);

}
