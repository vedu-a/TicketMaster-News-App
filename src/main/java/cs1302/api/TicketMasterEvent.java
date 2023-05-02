package cs1302.api;

/**
 * Represents a TicketMasterEvent object.
 */
public class TicketMasterEvent {
    String name;

    @Override
    public boolean equals(Object other) {
        if (name != null && other instanceof TicketMasterEvent) {
            TicketMasterEvent otherEvent = (TicketMasterEvent) other;
            return name.equals(otherEvent.name);
        }

        return false;
    }
}
