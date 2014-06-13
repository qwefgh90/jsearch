/**
 * 
 */
package com.d2.osfad.job;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystem;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.job.SFileFilter.EXTENSIONS;

/**
 * @author Chang
 * for using EXTENSIONS variable, make a new File Class
 */
public class DocumentFile extends File {
	public static final Logger log = LoggerFactory.getLogger(DocumentFile.class);
    
	public EXTENSIONS extension=null;
	public DocumentFile(String pathname,EXTENSIONS extension) {
		super(pathname);
		this.extension = extension;
		// TODO Auto-generated constructor stub
	}
	public DocumentFile(String pathname){
		super(pathname);
	}

	/**
	 * for make a DocumentFile instance
	 */
    public DocumentFile[] listDocFiles() {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
        EXTENSIONS temp_extension;
        for (String s : ss)
            if ((temp_extension = accept(s))!=null)
                files.add(new DocumentFile(this.getAbsolutePath()+'\\'+s,temp_extension));
        return files.toArray(new DocumentFile[files.size()]);
        
        
    }
    /**
     * for checking document
     */
    public DocumentFile[] listDocFiles(FileFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
        for (String s : ss) {

        	DocumentFile f = new DocumentFile(this.getAbsolutePath()+'\\'+s);
//
            if ((filter == null) || filter.accept(f)){
                files.add(f);
            }
        }
        return files.toArray(new DocumentFile[files.size()]);
    }
    
    /**
     * Change File filter implementation
     */
	private int i = 0;
	private int j = 0;
	private int numOfExtension = EXTENSIONS.values().length;
	private EXTENSIONS[] extensions = EXTENSIONS.values();
	/**
	 * arg1 file name
	 * check files extension
	 */
	public EXTENSIONS accept(String arg1) {
		for (i = 0; i < numOfExtension; i++) {
			for (j = 0; j < extensions[i].extension_count; j++) {
				if (arg1.endsWith( extensions[i].extension[j] )) {
					return extensions[i];
				}
			}
		}
		return null;
	}
}
