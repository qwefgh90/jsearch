package com.d2.osfad.job;

/**
 * @author Chang items to be stored in jobQueue contains file, jobid
 */
public interface IJobItem {
	enum JOBID {
		FINDKEYWORD("JobItemFile");

		private String className=null;
		JOBID(String arg) {
				this.className = arg ;
		}

		String getClassName() {
			return className;
		}
	}

	/**
	 * get job id
	 * 
	 * @return
	 */
	public JOBID getJobId();
}
