package iam.aws.store;

import java.util.List;

import iam.aws.enums.OperationType;
import iam.aws.enums.ScopeName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="scopes")
@NoArgsConstructor
public class Scope {
    @Id @Column(name="scopeID")
    private String scopeCode;

    @NonNull private ScopeName scopeName;
    @NonNull @Column(columnDefinition="TEXT") private String columns;
    @NonNull private OperationType operationType;

    public Scope(String sCode, String scopeName, String opType) {
        this.scopeCode = sCode;
        this.scopeName = ScopeName.valueOf(scopeName.toUpperCase());
        this.operationType = OperationType.valueOf(opType.toUpperCase());
        this.columns = "";
    }

    public Scope(String sCode, ScopeName name, OperationType opType) {
        this.scopeCode = sCode;
        this.scopeName = name;
        this.operationType = opType;
        this.columns = "";
    }

    public void setColumns(List<String> cols) {
        columns = String.join(",", cols);
    }
}
