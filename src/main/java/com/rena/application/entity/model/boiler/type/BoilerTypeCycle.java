package com.rena.application.entity.model.boiler.type;

import com.rena.application.entity.model.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler_type_сycle", indexes = {
        @Index(name = "idx_boiler_type_history_article_model", columnList = "model"),
        @Index(name = "idx_boiler_type_history_name_type_name", columnList = "type_name"),
        @Index(name = "idx_boiler_type_history_article", columnList = "article"),
        @Index(name = "idx_boiler_type_history_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_boiler_type_history_is_active", columnList = "is_active"),
        @Index(name = "idx_boiler_type_history_boiler_type_id", columnList = "boiler_type_id")
})
public class BoilerTypeCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_type_id", nullable = false)
    private Long boilerTypeId;

    @NotNull
    @Column(name = "type_name", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String typeName;

    @NotNull
    @Column(name = "model", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String model;

    @NotNull
    @Column(name = "article", nullable = false, length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String article;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @JdbcTypeCode(SqlTypes.BIT)
    private Boolean isActive = false;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_history_id")
    private UserHistory userHistory;
}