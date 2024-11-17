package ru.netology.transferMoney;

import org.springframework.boot.SpringApplication;

public class TestTransferMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.from(TransferMoneyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
