package com.bt.biztaker.qrcode;

public class QRObject {

	public Long entityId;
	public String entity;
	public Long generatorId;
	
	public QRObject(Long entityId,String entity,Long generatorId) {
		this.entityId = entityId;
		this.entity = entity;
		this.generatorId = generatorId;
	}
}
