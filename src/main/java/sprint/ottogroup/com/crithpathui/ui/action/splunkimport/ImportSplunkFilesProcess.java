package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.JobRun;
import sprint.ottogroup.com.crithpathui.data.Repository;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;
import sprint.ottogroup.com.crithpathui.ui.ProgressBarFrame;
import sprint.ottogroup.com.crithpathui.ui.action.splunkimport.CsvTable.TableRow;

public class ImportSplunkFilesProcess {

	private final File[] files;
	private final MainFrame mainFrame;
	private final ImportFilesTask task;
	private ProgressBarFrame progressMonitor;

	public ImportSplunkFilesProcess(File[] files, MainFrame mainFrame) {
		this.files = files;
		this.mainFrame = mainFrame;
		this.task = new ImportFilesTask(mainFrame.getConfig().getRepository(), files);
	}

	public void start() {
		if (files == null || files.length == 0) {
			return;
		}

		progressMonitor = new ProgressBarFrame("Import Splunk Files", task, files.length);
		mainFrame.addFrameCentered(progressMonitor);
		task.execute();
	}

	private class ImportFilesTask extends SwingWorker<Void, TempResult> {
		private final File[] files;
		private final Repository repository;
		private final Gson gson;
		private int cntPath = 0;
		private int cntJobRuns = 0;

		public ImportFilesTask(Repository repo, File[] files) {
			this.repository = repo;
			this.files = files;
			this.gson = new GsonBuilder().create();
		}

		@Override
		protected Void doInBackground() throws Exception {
			setProgress(0);
			int progress = 0;
			for (File file : files) {
				if (!isCancelled()) {
					processFile(file);
					++progress;
					setProgress(progress);
				}
			}

			return null;
		}
		
		@Override
		protected void done() {
			progressMonitor.log(String.format("%,d critical paths loaded.", cntPath));
			progressMonitor.log(String.format("%,d job executions loaded.", cntJobRuns));
		}

		private void processFile(File file) {
			progressMonitor.log("load file " + file.getAbsolutePath() + " ...");
			LineNumberReader lnr = null;

			try {
				lnr = new LineNumberReader(new FileReader(file));
				String line = null;

				while ((!isCancelled()) && ((line = lnr.readLine()) != null)) {
					processLine(line);
				}
			} catch (IOException e) {
				progressMonitor.log("[ERROR] " + e);
			} finally {
				if (lnr != null) {
					try {
						lnr.close();
					} catch (IOException e) {
					}
				}
			}
		}

		private void processLine(String line) {
			try {
				SplunkWithRawPath rawPath = gson.fromJson(line, SplunkWithRawPath.class);

				if (rawPath.result.isFilled()) {
					CritPath path = convert(rawPath.result);
					if (path != null) {
						publish(new TempResult(path));
					}
				}
			} catch (Exception ex) {
				progressMonitor.log("[ERROR] " + ex);
			}

			try {
				SplunkWithRawJobRun rawJobRun = gson.fromJson(line, SplunkWithRawJobRun.class);
				if (rawJobRun.result.isFilled()) {
					JobRun path = convert(rawJobRun.result);
					if (path != null) {
						publish(new TempResult(path));
					}
				}
			} catch (Exception ex) {
				progressMonitor.log("[ERROR] " + ex);
			}
		}

		private final SimpleDateFormat sdfOdate = new SimpleDateFormat("yyyyMMdd");
		private final SimpleDateFormat sdfStartStop = new SimpleDateFormat("yyyyMMddHHmmss");

		private JobRun convert(RawJobRun rawJobRun) throws ParseException {
			JobRun result = new JobRun();
			result.setControlMName(rawJobRun.controlMName);
			result.setGroupName(rawJobRun.groupName);
			result.setTableName(rawJobRun.tableName);
			result.setShellscript(rawJobRun.shellscript);

			result.setOdate(sdfOdate.parse(rawJobRun.odate));
			result.setStartingTime(sdfStartStop.parse(rawJobRun.startingTime));
			result.setFinishedTime(sdfStartStop.parse(rawJobRun.finishedTime));
			result.setDurationInSeconds(Integer.parseInt(rawJobRun.durationInSeconds));
			result.setRerunNo(Integer.parseInt(rawJobRun.rerunNo));
			result.setReturnCode(Integer.parseInt(rawJobRun.returnCode));

			return result;
		}

		private JobRun convert(TableRow row) throws ParseException {
			JobRun result = new JobRun();
			result.setControlMName(row.get("JOBNAME"));
			result.setGroupName(row.get("GROUP"));
//			result.setTableName(rawJobRun.tableName);
			result.setShellscript(row.get("SCRIPTNAME"));

			result.setOdate(sdfOdate.parse(row.get("ODATE")));
			result.setStartingTime(sdfStartStop.parse(row.get("STARTZEIT")));
			result.setFinishedTime(sdfStartStop.parse(row.get("STOPZEIT")));
			result.setDurationInSeconds(Integer.parseInt(row.get("LAUFZEIT")));
			result.setRerunNo(Integer.parseInt(row.get("RUNCOUNT")));
			result.setReturnCode(Integer.parseInt(row.get("RCODE")));

			return result;
		}

		private LineSplitter lineSplitter = new LineSplitter();
		private PathNameExtractor pathNameExtractor = new PathNameExtractor();
		private CsvExtractor pathTable = new CsvExtractor();
		
		private CritPath convert(RawPath rawPath) throws ParseException {
			CritPath path = new CritPath();

			Date odate = sdfOdate.parse(rawPath.odate);
			path.setOdate(odate);

			// convert sysout to real lines
			String[] sysoutLines = lineSplitter.split(rawPath.sysout);

			// find pathname
			String pathName = pathNameExtractor.extractPathName(sysoutLines);
			path.setName(pathName);

			// find path
			List<String[]> tableData = pathTable.extractCsv(sysoutLines);
			CsvTable csvTable = new CsvTable(tableData);
			for (TableRow row: csvTable) {
				path.addJob(convert(row));
			}

			return path;
		}

		@Override
		protected void process(List<TempResult> chunks) {
			// process intermediate results
			for (TempResult tempResult : chunks) {
				if (tempResult.jobRun != null) {
					repository.add(tempResult.jobRun);
					++cntJobRuns ;
				}
				if (tempResult.path != null) {
					repository.add(tempResult.path);
					++cntPath;
				}
			}
		}
	}

	private static class TempResult {
		public final JobRun jobRun;
		public final CritPath path;

		public TempResult(JobRun run) {
			this.path = null;
			this.jobRun = run;
		}

		public TempResult(CritPath path) {
			this.path = path;
			this.jobRun = null;
		}
	}

	public static class SplunkWithRawPath {
		@SerializedName("result")
		RawPath result = new RawPath();		
	}
	
	public static class RawPath {
		@SerializedName("ODATE")
		String odate;
		@SerializedName("_raw")
		String sysout;
		@SerializedName("env")
		String env;
		@SerializedName("linecount")
		String linecount;

		public boolean isFilled() {
			return (odate != null && sysout != null);
		}
	}

	public static class SplunkWithRawJobRun {
		@SerializedName("result")
		RawJobRun result = new RawJobRun();				
	}

	public static class RawJobRun {
		@SerializedName("ODATE")
		String odate;
		@SerializedName("GRUPPE")
		String groupName;
		@SerializedName("JOBNAME")
		String controlMName;
		@SerializedName("LAUFZEIT_SEC")
		String durationInSeconds;
		@SerializedName("RERUNNO")
		String rerunNo;
		@SerializedName("SCHEDTAB")
		String tableName;
		@SerializedName("SCRIPTNAME")
		String shellscript;
		@SerializedName("STARTZEIT")
		String startingTime;
		@SerializedName("STOPZEIT")
		String finishedTime;
		@SerializedName("RCODE")
		String returnCode;

		public boolean isFilled() {
			return (odate != null && controlMName != null && startingTime != null && finishedTime != null);
		}
	}
}
