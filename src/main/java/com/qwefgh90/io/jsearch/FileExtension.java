package com.qwefgh90.io.jsearch;

/**
 * This enum class represents various extensions and contains utility functions.
 * <br><br>
 * HWP, DOC, PPT, EXCEL, TEXT, PDF and UNKNOWN are supported.
 * @author cheochangwon
 *
 */
public enum FileExtension {
	HWP(".hwp"), DOC(".docx",".doc"), PPT(".pptx", ".ppt"), EXCEL(".xls",".xlsx"), TEXT(".txt"), PDF(".pdf"), UNKNOWN("");
	public String[] extension = null;
	public int extension_count;

	FileExtension(String... str)
	{
		this.extension = str;
		this.extension_count = str.length;
	};
	
	/**
	 * Find enum object with extension string
	 * @param fileName extension string
	 * @return FileExtension instance
	 */
	public static FileExtension getFileFormatbyExtension(String fileName)
	{
		FileExtension[] exArray = FileExtension.values();
		int len = exArray.length;
		for(int i = 0; i < len; i++){
			String[] strArray = exArray[i].extension;
			int lenOfString = strArray.length;
			for(int j = 0; j < lenOfString; j++){
				if(fileName.toLowerCase().endsWith(strArray[j].toLowerCase())){
					//get enum object
					return exArray[i];
				}
			}
		}
		//cant find matching extension
		return UNKNOWN;
	}
	
	/**
	 * Find enum object with extension string
	 * @param fileName extension string
	 * @return FileExtension instance
	 */
	public static FileExtension getFileFormatbyExtensionIgnorePostfix(String fileName)
	{
		FileExtension[] exArray = FileExtension.values();
		int len = exArray.length;
		for(int i = 0; i < len; i++){
			String[] strArray = exArray[i].extension;
			int lenOfString = strArray.length;
			for(int j = 0; j < lenOfString; j++){
				if(fileName.toLowerCase().contains(strArray[j].toLowerCase())){
					//get enum object
					return exArray[i];
				}
			}
		}
		//cant find matching extension
		return UNKNOWN;
	}
	
}
