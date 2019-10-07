package loganalyzer;

final class LogAnalyzer {
    private final LogRepository logRepository;
    private final LogHandle logHandle;
    private final OpsSupport opsSupport;

    public LogAnalyzer(LogRepository logRepository, EventRepository eventRepository, OpsSupport opsSupport) {
        this.logRepository = logRepository;
        this.logHandle = new ThreadedLogHandle(new DefaultLogHandle(eventRepository, FlagEventStrategy.LONG_EVENT));
        this.opsSupport = opsSupport;
    }

    public void run() {
        opsSupport.startedProcessing();
        logRepository.forEachFoundLog(logHandle::handle);
        logHandle.finish();
        opsSupport.finishedProcessing();
    }
}
