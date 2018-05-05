package com.mydomain.game.tambola;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeech {

	private static final String VOICENAME = "kevin16";

	public void convertTextToSpeech(int num) {

		Voice voice;
		VoiceManager voiceManager = VoiceManager.getInstance();

		voice = voiceManager.getVoice(VOICENAME);
		voice.allocate();

		int quotient = -1;
		int modulo = -1;
		if (num >= 10) {
			quotient = num / 10;
			modulo = num % 10;
		}
		System.out.println("num = " + num);
		try {
			Thread.sleep(600);
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		}
		if (quotient != -1 && modulo != -1) {
			voice.speak("" + quotient + "");
			voice.speak("" + modulo + "");
			voice.speak("" + num + "");
		} else {
			voice.speak("Single Number... " + num + "");
		}
	}
}
