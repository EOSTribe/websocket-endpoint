package eosio.spectrum.websocket.api.Message.chronicle;

import java.util.List;

public class Partial {
    private List<Context_free_data> context_free_data;
    private int delay_sec;
    private String expiration;
    private int max_cpu_usage_ms;
    private int max_net_usage_words;
    private int ref_block_num;
    private int ref_block_prefix;
    private List<String> signatures;
    private List<Transaction_extensions> transaction_extensions;

    public List<Context_free_data> getContext_free_data() {
        return context_free_data;
    }

    public void setContext_free_data(List<Context_free_data> context_free_data) {
        this.context_free_data = context_free_data;
    }

    public int getDelay_sec() {
        return delay_sec;
    }

    public void setDelay_sec(int delay_sec) {
        this.delay_sec = delay_sec;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getMax_cpu_usage_ms() {
        return max_cpu_usage_ms;
    }

    public void setMax_cpu_usage_ms(int max_cpu_usage_ms) {
        this.max_cpu_usage_ms = max_cpu_usage_ms;
    }

    public int getMax_net_usage_words() {
        return max_net_usage_words;
    }

    public void setMax_net_usage_words(int max_net_usage_words) {
        this.max_net_usage_words = max_net_usage_words;
    }

    public int getRef_block_num() {
        return ref_block_num;
    }

    public void setRef_block_num(int ref_block_num) {
        this.ref_block_num = ref_block_num;
    }

    public int getRef_block_prefix() {
        return ref_block_prefix;
    }

    public void setRef_block_prefix(int ref_block_prefix) {
        this.ref_block_prefix = ref_block_prefix;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public List<Transaction_extensions> getTransaction_extensions() {
        return transaction_extensions;
    }

    public void setTransaction_extensions(List<Transaction_extensions> transaction_extensions) {
        this.transaction_extensions = transaction_extensions;
    }
}
