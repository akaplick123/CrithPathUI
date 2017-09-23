package sprint.ottogroup.com.crithpathui.data;

import java.util.Comparator;
import java.util.Date;

public class JobRun {
	public static final Comparator<JobRun> COMPARE_BY_NAME_STARTTIME_FINISHTIME = new CompareByNameAndStarttime();

	private Date odate;
	private String groupName = "N/A";
	private String controlMName;
	private int durationInSeconds;
	private int rerunNo;
	private String tableName = "N/A";
	private String shellscript;
	private Date startingTime;
	private Date finishedTime;
	private int returnCode;

	public Date getOdate() {
		return odate;
	}

	public void setOdate(Date odate) {
		this.odate = odate;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getControlMName() {
		return controlMName;
	}

	public void setControlMName(String controlMName) {
		this.controlMName = controlMName;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	public int getRerunNo() {
		return rerunNo;
	}

	public void setRerunNo(int rerunNo) {
		this.rerunNo = rerunNo;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getShellscript() {
		return shellscript;
	}

	public void setShellscript(String shellscript) {
		this.shellscript = shellscript;
	}

	public Date getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public static class CompareByNameAndStarttime implements Comparator<JobRun> {

		public int compare(JobRun o1, JobRun o2) {
			int result = 0;
			if (result == 0) {
				result = o1.getControlMName().compareToIgnoreCase(o2.getControlMName());
			}
			if (result == 0) {
				result = o1.getStartingTime().compareTo(o2.getStartingTime());
			}
			if (result == 0) {
				result = o1.getFinishedTime().compareTo(o2.getFinishedTime());
			}
			return result;
		}

	}

}
