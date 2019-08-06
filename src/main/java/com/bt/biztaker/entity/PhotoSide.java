package com.bt.biztaker.entity;

public enum PhotoSide {

	FRONT_SIDE(0), BACK_SIDE(1);

	public int value;

	private PhotoSide(int value) {
		this.value = value;
	}
}
