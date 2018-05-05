package com.mydomain.game.tambola;

import java.util.Random;

public class RandomNumberGenerator {
	
	int[] array = new int[90];

	private int getRandomNumber() {
		Random rand = new Random();
		
		int num = rand.nextInt(90) + 1;
		return num;
	}

	public int getNextNumberToSpeak() {	
		
		int randomNumber = getRandomNumber();
						
		boolean present = isExists(randomNumber);
		if(!present) {
			this.array[randomNumber-1] = randomNumber;
		} else {
			randomNumber = getNextNumberToSpeak();
		}		
		return randomNumber;
	}

	private boolean isExists(int num) {
		if (this.array[num-1] == num) {
			return true;
		}
		return false;
	}
}
