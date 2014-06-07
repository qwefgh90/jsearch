package com.d2.osfad.job;

import java.io.File;

public class JobItemFile implements IJobItem {
	public final JOBID id = JOBID.FINDKEYWORD;
	private File[] fileList=null;
	public int startOff = 0;		/* equal or greater than startOff */
	public int endOff = 0;			/* less than end off */
	public JobItemFile(File[] fileList,int start, int end){
		this.fileList = fileList;
		startOff=start;
		endOff=end;
	}
	@Override
	public final JOBID getJobId() {
		// TODO Auto-generated method stub
		return id;
	}

	public final File[] getFileList() {
		// TODO Auto-generated method stub
		return fileList;
	}
}
