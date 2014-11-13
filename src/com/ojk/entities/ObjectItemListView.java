package com.ojk.entities;

import java.io.Serializable;

public class ObjectItemListView implements Serializable {
    
	private static final long serialVersionUID = 5046682058900906102L;
	
	public int itemId;
    public String itemName;
    public String url;
    public int idParent;
    public int isLastChild;
    public int anakCount;
    
    //Tambahan untuk OJKTerbaru
    public String itemType;
    public String downloadUrl;
    public String fileSize;
    public String fileType;
    public String parentUrl;
    public String created;
    
    //Khusus untuk OJKTerbaru
    public ObjectItemListView(String itemType, String itemName, String downloadUrl, String fileSize, String fileType, String parentUrl, String url, String created){
    	this.itemType = itemType;
    	this.itemName = itemName;
    	this.downloadUrl = downloadUrl;
    	this.fileSize = fileSize;
    	this.fileType = fileType;
    	this.parentUrl = parentUrl;
    	this.url = url;
    	this.created = created;
    }
    
    public ObjectItemListView(int itemId, String itemName, String url, int idParent, int isLastChild ,int anakCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.url = url;
        this.idParent = idParent;
        this.isLastChild = isLastChild;
        this.anakCount = anakCount;
    }
    
    public ObjectItemListView(int itemId, String itemName, String url, int idParent, int isLastChild) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.idParent = idParent;
        this.url = url;
        this.isLastChild = isLastChild;
    }
    
    public ObjectItemListView(int itemId, String itemName, String url, int idParent) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.idParent = idParent;
        this.url = url;
    }
    
    public ObjectItemListView(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }
}
