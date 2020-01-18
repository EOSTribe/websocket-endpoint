package eosio.spectrum.websocket.api.message.eosio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

public class Block {
    private List<Header_extensions> header_extensions;
    private String new_producesrs;
    private String previous;
    private List<String> block_extensions;
    private int schedule_version;
    private String prodcuer;
    private String transaction_mroot;
    private String producer_signature;
    @SerializedName("transactions")
    private List<Transactions> transactions;
    private int confirmed;
    private String action_mroot;
    private String timestamp;
    private int block_num;

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public int getLas_irreversible() {
        return las_irreversible;
    }

    public void setLas_irreversible(int las_irreversible) {
        this.las_irreversible = las_irreversible;
    }

    private int las_irreversible;


    public List<Header_extensions> getHeader_extensions() {
        return header_extensions;
    }

    public void setHeader_extensions(List<Header_extensions> header_extensions) {
        this.header_extensions = header_extensions;
    }

    public String getNew_producesrs() {
        return new_producesrs;
    }

    public void setNew_producesrs(String new_producesrs) {
        this.new_producesrs = new_producesrs;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<String> getBlock_extensions() {
        return block_extensions;
    }

    public void setBlock_extensions(List<String> block_extensions) {
        this.block_extensions = block_extensions;
    }

    public int getSchedule_version() {
        return schedule_version;
    }

    public void setSchedule_version(int schedule_version) {
        this.schedule_version = schedule_version;
    }

    public String getProdcuer() {
        return prodcuer;
    }

    public void setProdcuer(String prodcuer) {
        this.prodcuer = prodcuer;
    }

    public String getTransaction_mroot() {
        return transaction_mroot;
    }

    public void setTransaction_mroot(String transaction_mroot) {
        this.transaction_mroot = transaction_mroot;
    }

    public String getProducer_signature() {
        return producer_signature;
    }

    public void setProducer_signature(String producer_signature) {
        this.producer_signature = producer_signature;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getAction_mroot() {
        return action_mroot;
    }

    public void setAction_mroot(String action_mroot) {
        this.action_mroot = action_mroot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
