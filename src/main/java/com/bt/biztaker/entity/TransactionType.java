package com.bt.biztaker.entity;

public enum TransactionType {

	CREDIT(0), DEBIT(1);

	public int value;

	private TransactionType(int value) {
		this.value = value;
	}
}
