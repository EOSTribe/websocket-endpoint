package eosio.spectrum.websocket.api.message.eosio;

import java.util.List;

public class Receipt {
    private String receiver;
    private int code_sequence;
    private int abi_sequence;
    private int recv_sequence;
    private List<Auth_sequence> auth_sequence;
    private String act_digest;
    private int global_sequence;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getCode_sequence() {
        return code_sequence;
    }

    public void setCode_sequence(int code_sequence) {
        this.code_sequence = code_sequence;
    }

    public int getAbi_sequence() {
        return abi_sequence;
    }

    public void setAbi_sequence(int abi_sequence) {
        this.abi_sequence = abi_sequence;
    }

    public int getRecv_sequence() {
        return recv_sequence;
    }

    public void setRecv_sequence(int recv_sequence) {
        this.recv_sequence = recv_sequence;
    }

    public List<Auth_sequence> getAuth_sequence() {
        return auth_sequence;
    }

    public void setAuth_sequence(List<Auth_sequence> auth_sequence) {
        this.auth_sequence = auth_sequence;
    }

    public String getAct_digest() {
        return act_digest;
    }

    public void setAct_digest(String act_digest) {
        this.act_digest = act_digest;
    }

    public int getGlobal_sequence() {
        return global_sequence;
    }

    public void setGlobal_sequence(int global_sequence) {
        this.global_sequence = global_sequence;
    }
}
