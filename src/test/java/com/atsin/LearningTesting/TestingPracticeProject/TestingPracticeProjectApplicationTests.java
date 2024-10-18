package com.atsin.LearningTesting.TestingPracticeProject;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@SpringBootTest
@Slf4j
class TestingPracticeProjectApplicationTests {

	@BeforeEach
	void beforeEachTest(){
		log.info("Starting the method, setting up configuration");
	}

	@BeforeAll
	static void beforeAllTest(){
		log.info("Setup Once....");
	}

	@AfterEach
	void afterEachTest(){
		log.info("Tearing down the method");
	}

	@AfterAll
	static void afterAllTest(){
		log.info("Tearing down all the method");
	}

	@Test
	void testDivideTwoNumbers_whenDenominatiorIsZero_ThenArithmeticException(){

		int a=5;
		int b=0;

		assertThatThrownBy(() -> divideTwoNumbers(a, b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");
	}

	int addTwoNumbers(int a, int b) {
		return a + b;
	}

	double divideTwoNumbers(int a, int b) {
		try {
			return a/b;
		}catch (ArithmeticException e){
			log.error("ArithmeticException occured: "+e.getMessage());
			throw new ArithmeticException("Tried to divide by zero");
		}
	}



}
