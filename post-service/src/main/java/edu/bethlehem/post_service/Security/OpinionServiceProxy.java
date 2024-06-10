package edu.bethlehem.post_service.Security;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "opinion-service")
@EnableDiscoveryClient
public interface OpinionServiceProxy {

    @GetMapping("/auth/extractUsername")
    public ResponseEntity<?> extractUsername(String jwt);

    @PostMapping(value = "/auth/extractUserId", consumes = "application/json", produces = "application/json")
    public Long extractUserId(@RequestBody String jwt);

}
