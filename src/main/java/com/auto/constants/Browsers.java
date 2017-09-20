package com.auto.constants;

public enum Browsers {

	CH,
	HD,
	IE,
	SF,
	FF;
	

	public static Browsers browserForName(String browser) throws IllegalArgumentException{
        for(Browsers b: values()){
    		if(b.toString().equalsIgnoreCase(browser)){
    			return b;
    		}
        }
        throw browserNotFound(browser);
    }

    private static IllegalArgumentException browserNotFound(String outcome) {
        return new IllegalArgumentException(("Invalid browser [" + outcome + "]"));
    }
}
