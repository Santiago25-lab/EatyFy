package com.myapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.myapp.entity.MenuItem;
import com.myapp.entity.Restaurant;
import com.myapp.service.MenuItemService;
import com.myapp.service.RestaurantService;

@SpringBootApplication
@ComponentScan(basePackages = "com.myapp")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(RestaurantService restaurantService, MenuItemService menuItemService) {
		return args -> {
			if (restaurantService.getRestaurants().isEmpty()) {
				// Add sample restaurants
				Restaurant r1 = new Restaurant();
				r1.setName("La Creperia");
				r1.setAddress("Bogotá, Colombia");
				r1.setCuisineType("Francesa");
				r1.setAveragePricePerPerson(45000.0);
				r1.setPhone("+57 1 2345678");
				r1.setWebsite("https://creperia.com");
				r1.setLatitude(4.711);
				r1.setLongitude(-74.0721);
				restaurantService.saveRestaurant(r1);

				// Add menu items for r1
				MenuItem m1 = new MenuItem();
				m1.setName("Crepe de Nutella");
				m1.setDescription("Delicioso crepe con nutella y plátano");
				m1.setPrice(25000.0);
				m1.setCategory("Postre");
				m1.setRestaurant(r1);
				menuItemService.saveMenuItem(m1);

				MenuItem m2 = new MenuItem();
				m2.setName("Crepe Salado");
				m2.setDescription("Crepe con jamón, queso y verduras");
				m2.setPrice(35000.0);
				m2.setCategory("Plato principal");
				m2.setRestaurant(r1);
				menuItemService.saveMenuItem(m2);

				Restaurant r2 = new Restaurant();
				r2.setName("Taco Loco");
				r2.setAddress("Medellín, Colombia");
				r2.setCuisineType("Mexicana");
				r2.setAveragePricePerPerson(35000.0);
				r2.setPhone("+57 4 8765432");
				r2.setWebsite("https://tacoloco.com");
				r2.setLatitude(6.2442);
				r2.setLongitude(-75.5812);
				restaurantService.saveRestaurant(r2);

				// Add menu items for r2
				MenuItem m3 = new MenuItem();
				m3.setName("Tacos al Pastor");
				m3.setDescription("3 tacos de cerdo marinado con piña");
				m3.setPrice(28000.0);
				m3.setCategory("Plato principal");
				m3.setRestaurant(r2);
				menuItemService.saveMenuItem(m3);

				MenuItem m4 = new MenuItem();
				m4.setName("Quesadilla");
				m4.setDescription("Quesadilla de pollo con guacamole");
				m4.setPrice(22000.0);
				m4.setCategory("Entrada");
				m4.setRestaurant(r2);
				menuItemService.saveMenuItem(m4);

				Restaurant r3 = new Restaurant();
				r3.setName("Sushi Master");
				r3.setAddress("Cali, Colombia");
				r3.setCuisineType("Japonesa");
				r3.setAveragePricePerPerson(55000.0);
				r3.setPhone("+57 2 3456789");
				r3.setWebsite("https://sushimaster.com");
				r3.setLatitude(3.4372);
				r3.setLongitude(-76.5225);
				restaurantService.saveRestaurant(r3);

				// Add menu items for r3
				MenuItem m5 = new MenuItem();
				m5.setName("Sushi Roll California");
				m5.setDescription("8 piezas de sushi roll con aguacate y cangrejo");
				m5.setPrice(45000.0);
				m5.setCategory("Plato principal");
				m5.setRestaurant(r3);
				menuItemService.saveMenuItem(m5);

				MenuItem m6 = new MenuItem();
				m6.setName("Sashimi Mixto");
				m6.setDescription("Variedad de pescados frescos");
				m6.setPrice(65000.0);
				m6.setCategory("Plato principal");
				m6.setRestaurant(r3);
				menuItemService.saveMenuItem(m6);


				System.out.println("Sample restaurants and menu items added to database");
			}
		};
	}

}
