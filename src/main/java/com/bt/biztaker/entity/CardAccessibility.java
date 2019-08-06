package com.bt.biztaker.entity;

public enum CardAccessibility {

	PUBLIC(0),PRIVATE(1);
	
	public int value;

	private CardAccessibility(int value) {
		this.value = value;
	}
}
