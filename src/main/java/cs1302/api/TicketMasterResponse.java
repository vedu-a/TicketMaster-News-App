package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the TicketMaster API.
 */
public class TicketMasterResponse {
    @SerializedName("_embedded") TicketMasterEmbedded embedded;
}
