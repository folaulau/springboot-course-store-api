package com.springboot.product.size;

public enum MonomonoSize {

	CRIB(45,60,"CRIB"),
	TWIN(60,90,"TWIN"),
	FULL(80,90,"FULL"),
	QUEEN(90,90,"QUEEN"),
	KING(108,90,"KING"),
	CALI_KING(108,108,"CALI_KING");
	
	private int width;
	private int height;
	private String title;
	
	MonomonoSize(int width, int height, String title){
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
		
		if(size.equals(CRIB.title)||size.equals(TWIN.title)||
				size.equals(FULL.title)||
				size.equals(QUEEN.title)||
				size.equals(KING.title)||
				size.equals(CALI_KING.title)) {
			return true;
		}
		
		return false;
	}
	
}
