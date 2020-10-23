package com.circuitbreaker.circuitBreaker;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
@RestController
public class CircuitBreakerApplication {

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



	@GetMapping("/getAllPoducts")
	@HystrixCommand(fallbackMethod = "callGetProducts_Fallback")
	public String callGetProducts(){

		String url = "http://localhost:9090/Produits/";
		String response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
		return "Normal flow: Product List : " + response + "-";
	}

	public String callGetProducts_Fallback(){
		System.out.println("Service is down");
		return "No response";
	}

	@GetMapping("/getpoductById/{id}")
	@HystrixCommand(fallbackMethod = "callGetById_Fallback")
	public String callGetById(int id){

		String url = "http://localhost:9090/getById/{id}";
		String response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();
		return "Normal flow: Product List : " + response + "-";
	}

	public String callGetById_Fallback(int id){
		System.out.println("Service is down");
		return "No response";
	}

	@GetMapping("/deletePoductById/{id}")
	@HystrixCommand(fallbackMethod = "callDeleteById_Fallback")
	public String callDeleteById(int id){

		String url = "http://localhost:9090/DeleteById/{id}";
		String response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();
		return "Normal flow: Product List : " + response + "-";
	}

	public String callDeleteById_Fallback(int id){
		System.out.println("Service is down");
		return "No response";
	}



	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerApplication.class, args);
	}

}
