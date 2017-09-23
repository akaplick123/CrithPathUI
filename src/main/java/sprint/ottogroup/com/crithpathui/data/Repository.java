package sprint.ottogroup.com.crithpathui.data;

import java.util.HashSet;
import java.util.Set;

public class Repository {

	private final Set<JobRun> runs = new HashSet<JobRun>();
	private final Set<CritPath> paths = new HashSet<CritPath>();
	
	public Repository() {
	}

	public Set<JobRun> getRuns() {
		synchronized (runs) {
			return new HashSet<JobRun>(runs);
		}
	}
	
	public void add(JobRun jobRun) {
		synchronized (this.runs) {
			this.runs.add(jobRun);
		}
	}
	
	public Set<CritPath> getPaths() {
		synchronized (paths) {
			return new HashSet<CritPath>(paths);
		}
	}
	
	public void add(CritPath critPath) {
		if (critPath.numberOfJobs() < 1) {
			// do not add critpath without an path
			return;
		}

		synchronized (this.paths) {
			this.paths.add(critPath);
		}
	}
}
