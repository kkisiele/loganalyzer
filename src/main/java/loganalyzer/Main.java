package loganalyzer;

import loganalyzer.infrastructure.log.JsonLogRepository;
import loganalyzer.infrastructure.storage.Database;
import loganalyzer.infrastructure.storage.JdbcEventRepository;

import java.io.FileInputStream;

public final class Main {
    private static final String DEFAULT_DATABASE_NAME = "eventdb";

    private static final int LOG_FILE_NAME_PARAM_INDEX = 0;
    private static final int DATABASE_NAME_PARAM_INDEX = 1;

    public static void main(String... params) throws Exception {
        verifyProgramParameters(params);
        run(param(params, LOG_FILE_NAME_PARAM_INDEX), param(params, DATABASE_NAME_PARAM_INDEX));
    }

    private static void verifyProgramParameters(String[] params) {
        if(params.length == 0 || params.length > 2) {
            System.out.println("Program parameters are required: log_filename [database_name]");
            System.out.println("where:");
            System.out.println("log_filename is mandatory and specifies the server log input file name");
            System.out.println("database_name is optional and specifies the created database file names");

            System.exit(0);
        }
    }

    private static String param(String[] params, int index) {
        return params.length > index ? params[index] : null;
    }

    private static void run(String logFileName, String databaseName) throws Exception {
        final String connectionUrl = String.format(
                "jdbc:hsqldb:file:%s;shutdown=true",
                databaseName != null ? databaseName : DEFAULT_DATABASE_NAME
        );
        try(Database database = Database.setup(connectionUrl, "SA", "")) {
            OpsSupport opsSupport = new OpsSupport();
            LogRepository logRepository = new JsonLogRepository(new FileInputStream(logFileName), opsSupport);
            EventRepository eventRepository = new JdbcEventRepository(database, opsSupport);

            LogAnalyzer logAnalyzer = new LogAnalyzer(logRepository, eventRepository, opsSupport);
            logAnalyzer.run();
        }
    }
}
