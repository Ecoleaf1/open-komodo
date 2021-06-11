package net.wigoftime.open_komodo.etc;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

abstract public class DateCountSystem {

	public static Calendar addToCalendar(@NotNull String timeAdded) {
		String[] args = timeAdded.split(" ");
		
		Calendar cal = Calendar.getInstance();
		
		String[] amountStr = new String[args.length];
		int[] type = new int[args.length];
		System.out.println("int length: "+type.length);
		
		for (int i = 0; i < amountStr.length; i++) {
			amountStr[i] = "";
			
			int charIndex = 0;
			for (char c : args[i].toCharArray()) {
				if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c =='8' || c == '9' || c == '0') {
					amountStr[i] = amountStr[i]+Integer.parseInt(c+"");
					System.out.println(c);
					charIndex++;
					continue;
				}
				
				if (charIndex > 0) {
					if (c == 's' || c == 's') {
						type[i] = 1;
						charIndex++;
						break;
					}
					if (c == 'H' || c == 'h') {
						type[i] = 4;
						charIndex++;
						break;
					}
					if (c == 'W' || c == 'w') {
						type[i] = 5;
						charIndex++;
						break;
					}
					if (c == 'M' || c == 'm') {
						type[i] = 6;
						charIndex++;
						break;
					}
					
					if (c == 'Y' || c == 'y') {
						type[i] = 7;
						charIndex++;
						break;
					}
				}
				charIndex++;
				
			}
			
			if (type[i] == 1)
				cal.set(Calendar.SECOND, cal.get(Calendar.SECOND)+Integer.parseInt(amountStr[i]));
			if (type[i] == 3)
				cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+Integer.parseInt(amountStr[i]));
			
			if (type[i] == 4)
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+Integer.parseInt(amountStr[i]));
			
			if (type[i] == 5)
				cal.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH)+Integer.parseInt(amountStr[i]));
			
			if (type[i] == 6)
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+Integer.parseInt(amountStr[i]));
			
			if (type[i] == 7)
				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+Integer.parseInt(amountStr[i]));
			
			
		}
		
		return cal;
		
	}
	
}
