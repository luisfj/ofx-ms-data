package dev.luisjohann.ofxmsdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.ofxmsdata.model.ContaBancaria;
import dev.luisjohann.ofxmsdata.model.UnidadeEconomica;
import dev.luisjohann.ofxmsdata.model.User;
import dev.luisjohann.ofxmsdata.service.ContaBancariaService;
import dev.luisjohann.ofxmsdata.service.UnidadeEconomicaService;
import dev.luisjohann.ofxmsdata.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequiredArgsConstructor
public class UserController {

   final UserService service;
   final UnidadeEconomicaService ueService;
   final ContaBancariaService cbService;

   @GetMapping("/user/{name}")
   // @ResponseStatus(HttpStatus.OK)
   public Flux<User> getMethodName(@PathVariable("name") String name) {
      return service.findByNameContaining(name);
   }

   @GetMapping("/users")
   // @ResponseStatus(HttpStatus.OK)
   public Flux<User> getAll() {
      return service.findByNameContaining("ui");
   }

   @GetMapping("/ues")
   // @ResponseStatus(HttpStatus.OK)
   public Flux<UnidadeEconomica> getAllUe() {
      return ueService.findAll();
   }

   @GetMapping("/cbs")
   // @ResponseStatus(HttpStatus.OK)
   public Flux<ContaBancaria> getAllCbs() {
      return cbService.findAll();
   }

}
