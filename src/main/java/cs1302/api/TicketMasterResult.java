package cs1302.api;

/**
 * Respresents individual results from TicketMaster API.
 */
public class TicketMasterResult {
    @SerializedName("_embedded") TicketMasterResult embedded;
    String[] events;
    String name;
}
