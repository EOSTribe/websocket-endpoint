package eosio.spectrum.websocket.api.message.eosio;

import java.util.List;

public class Trx {
    private String signatures;
    private String compression;
    private String packed_context_free_data;
    private String packed_trx;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getPacked_context_free_data() {
        return packed_context_free_data;
    }

    public void setPacked_context_free_data(String packed_context_free_data) {
        this.packed_context_free_data = packed_context_free_data;
    }

    public String getPacked_trx() {
        return packed_trx;
    }

    public void setPacked_trx(String packed_trx) {
        this.packed_trx = packed_trx;
    }
}
