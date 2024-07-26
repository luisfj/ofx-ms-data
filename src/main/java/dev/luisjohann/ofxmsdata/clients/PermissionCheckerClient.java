package dev.luisjohann.ofxmsdata.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "permission-checker")
public interface PermissionCheckerClient {

      @RequestMapping(method = RequestMethod.GET, value = "/check-post-data/{user_id}/{ue_id}")
      void checkPostDataOfx(@PathVariable("user_id") Long userId, @PathVariable("ue_id") Long ueId);

      @RequestMapping(method = RequestMethod.GET, value = "/check-get-data/{user_id}/{ue_id}")
      Void checkGetDataPermission(@PathVariable("user_id") Long userId, @PathVariable("ue_id") Long ueId);
}
