package com.library.config;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.dialect.PostgreSQL10Dialect;

public class PostgreSQLCustomDialect extends PostgreSQL10Dialect {
    public PostgreSQLCustomDialect() {
        super();
        this.registerHibernateType(2003, StringArrayType.class.getName());
        this.registerHibernateType(1111, JsonNodeBinaryType.class.getName());
    }
}
