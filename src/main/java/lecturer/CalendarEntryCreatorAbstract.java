package lecturer;

import com.calendarfx.model.Entry;

public abstract class CalendarEntryCreatorAbstract {
    protected abstract Entry<?> createCalendarEntry(Calendar calendar1);

    protected void setStyleBasedOnStatus(Entry<?> entry, String status) {
        switch (status) {
            case "Approve":
                entry.getStyleClass().add("entry-approve");
                break;
            case "Pending":
                entry.getStyleClass().add("entry-pending");
                System.out.println("setPendingcolor");
                break;
            case "Reject":
                entry.getStyleClass().add("entry-reject");
                break;
            default:
                entry.getStyleClass().add("entry-default");
                break;
        }
        entry.getStyleClass().add("entry-black");
    }
}
