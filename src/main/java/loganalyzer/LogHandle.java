package loganalyzer;

interface LogHandle {
    void handle(Log log);
    void finish();
}
