package com.ojk.entities;

public class ObjectItemGridView {
	public int itemId;
    public String itemTitle;
    public String itemDownloadUrl;
    public String itemFileSize;
    public String itemFileType;
    public String itemParentUrl;
    public int itemIdParent;
    public int itemIsRead;
    public String createdOn;
    public String downloadedOn;
    public String itemUrl;
    
    public ObjectItemGridView(int itemId, String itemTitle, String itemDownloadUrl, String itemFileSize, String itemFileType, String itemParentUrl, int itemIdParent, int itemIsRead, String createdOn, String downloadedOn, String itemUrl ) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemDownloadUrl = itemDownloadUrl;
        this.itemFileSize = itemFileSize;
        this.itemFileType = itemFileType;
        this.itemParentUrl = itemParentUrl;
        this.itemIdParent = itemIdParent;
        this.itemIsRead = itemIsRead;
        this.createdOn = createdOn;
        this.downloadedOn = downloadedOn;
        this.itemUrl = itemUrl;
    }
    
    public ObjectItemGridView(String itemTitle, String itemDownloadUrl, String itemFileSize, String itemFileType) {
    	 this.itemTitle = itemTitle;
         this.itemDownloadUrl = itemDownloadUrl;
         this.itemFileSize = itemFileSize;
         this.itemFileType = itemFileType;
    }
}
