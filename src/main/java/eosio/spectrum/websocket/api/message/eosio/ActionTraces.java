package eosio.spectrum.websocket.api.message.eosio;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionTraces {
    private Boolean context_free;
    private int elapsed;
    private String console;
    private Act act;
    private int creator_action_ordinal;
    private String receiver;
    private int action_ordinal;
    private Receipt receipt;
    private String except;
    private String error_code;
    private List<Account_ram_deltas> account_ram_deltas;
    private int block_num;
    private String block_timestamp;
    @SerializedName("trxid")
    private String trxId;

    public Boolean getContext_free() {
        return context_free;
    }

    public void setContext_free(Boolean context_free) {
        this.context_free = context_free;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public int getCreator_action_ordinal() {
        return creator_action_ordinal;
    }

    public void setCreator_action_ordinal(int creator_action_ordinal) {
        this.creator_action_ordinal = creator_action_ordinal;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getAction_ordinal() {
        return action_ordinal;
    }

    public void setAction_ordinal(int action_ordinal) {
        this.action_ordinal = action_ordinal;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public String getExcept() {
        return except;
    }

    public void setExcept(String except) {
        this.except = except;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<Account_ram_deltas> getAccount_ram_deltas() {
        return account_ram_deltas;
    }

    public void setAccount_ram_deltas(List<Account_ram_deltas> account_ram_deltas) {
        this.account_ram_deltas = account_ram_deltas;
    }

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public String getBlock_timestamp() {
        return block_timestamp;
    }

    public void setBlock_timestamp(String block_timestamp) {
        this.block_timestamp = block_timestamp;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }
}
