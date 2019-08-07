package com.springboot.product.size;

public enum TShirtSize {

	SMALL(20,27,"SMALL"),
	MEDIUM(22,28,"MEDIUM"),
	LARGE(24,29,"LARGE"),
	XLARGE(26,30,"XLARGE"),
	X2LARGE(28,31,"2XLARGE"),
	X3LARGE(30,32,"3XLARGE"),
	X4LARGE(32,33,"4XLARGE"),
	X5LARGE(34,34,"5XLARGE");
	
	private int width;
	private int height;
	private String title;
	
	TShirtSize(int width, int height, String title){
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	
	public static boolean isValid(String size) {
		if(size==null || size.length()==0) {
			return false;
		}
		
		if(size.equals(SMALL.title)||size.equals(MEDIUM.title)||
				size.equals(LARGE.title)||
				size.equals(XLARGE.title)||
				size.equals(X2LARGE.title)||
				size.equals(X3LARGE.title)||
				size.equals(X4LARGE.title)||
				size.equals(X5LARGE.title)) {
			return true;
		}
		
		return false;
	}
	
}
