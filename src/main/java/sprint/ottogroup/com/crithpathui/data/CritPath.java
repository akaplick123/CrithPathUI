package sprint.ottogroup.com.crithpathui.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CritPath {
	public static final Comparator<CritPath> COMPARE_BY_NAME = new CompareByName();
	public static final Comparator<CritPath> COMPARE_BY_NAME_AND_ODATE = new CompareByNameAndOdate();

	private Date odate;
	private String name;
	private final List<JobRun> jobs = new ArrayList<JobRun>();

	public Date getOdate() {
		return odate;
	}

	public void setOdate(Date odate) {
		this.odate = odate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JobRun> getJobs() {
		return jobs;
	}

	public void addJob(JobRun jobRun) {
		this.jobs.add(jobRun);
	}

	public int numberOfJobs() {
		return jobs.size();
	}

	public Date firstStartingTime() {
		if (jobs.isEmpty()) {
			return null;
		}

		// calc minimum
		Date result = jobs.get(0).getStartingTime();
		for (JobRun job : jobs) {
			if (job.getStartingTime().before(result)) {
				result = job.getStartingTime();
			}
		}

		return result;
	}

	public Date lastFinishedTime() {
		if (jobs.isEmpty()) {
			return null;
		}

		// calc minimum
		Date result = jobs.get(0).getFinishedTime();
		for (JobRun job : jobs) {
			if (job.getFinishedTime().after(result)) {
				result = job.getFinishedTime();
			}
		}

		return result;
	}

	public int durationInSeconds() {
		Date start = firstStartingTime();
		Date finished = lastFinishedTime();

		if (start == null || finished == null) {
			return 0;
		}

		return (int) ((finished.getTime() - start.getTime()) / 1000L);
	}

	public static class CompareByName implements Comparator<CritPath> {
		public int compare(CritPath o1, CritPath o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
		}
	}

	public static class CompareByNameAndOdate implements Comparator<CritPath> {
		public int compare(CritPath o1, CritPath o2) {
			int result = 0;

			if (result == 0) {
				result = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
			}
			if (result == 0) {
				result = o1.getOdate().compareTo(o2.getOdate());
			}

			return result;
		}
	}

}
