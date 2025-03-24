package com.TLU.SoundVerse;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoundVerseApplication {
	static {
        Dotenv dotenv = Dotenv.load(); // Load biến môi trường từ `.env`
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

	public static void main(String[] args) {
		SpringApplication.run(SoundVerseApplication.class, args);
		System.out.print("Thanh cong");
	}

}
