package loganalyzer;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlagEventStrategyTest {
    private final FlagEventStrategy longEventAlertStrategy = FlagEventStrategy.LONG_EVENT;

    @Test
    public void setsFlagToTrueForLongEvents() {
        assertTrue(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(5).build()));
        assertTrue(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(50).build()));
        assertTrue(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(Integer.MAX_VALUE).build()));
    }

    @Test
    public void setsFlagToFalseForShortEvents() {
        assertFalse(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(4).build()));
        assertFalse(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(0).build()));
        assertFalse(longEventAlertStrategy.isFlagged(new Event.Builder().withDurationInMilliseconds(-1).build()));
    }}