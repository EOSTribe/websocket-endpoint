package eosio.spectrum.websocket.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GetTableRowsRule {
    private String code;
    private String scope;
    private String table;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GetTableRowsRule that = (GetTableRowsRule) o;

        return new EqualsBuilder()
                .append(getCode(), that.getCode())
                .append(getScope(), that.getScope())
                .append(getTable(), that.getTable())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCode())
                .append(getScope())
                .append(getTable())
                .toHashCode();
    }
}
