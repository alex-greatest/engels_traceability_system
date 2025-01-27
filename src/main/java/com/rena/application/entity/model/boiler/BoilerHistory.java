package com.rena.application.entity.model.boiler;

import com.rena.application.entity.model.component.ComponentNameSetHistory;
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
@Table(name = "boiler_history", indexes = {
        @Index(name = "idx_boiler_history_article_article_is_active", columnList = "article, is_active"),
        @Index(name = "idx_boiler_history_name_name", columnList = "name"),
        @Index(name = "idx_boiler_history_component_name_set", columnList = "component_name_set_history_id"),
        @Index(name = "idx_boiler_history_user_history_id", columnList = "user_history_id")
})
public class BoilerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_id", nullable = false)
    private Long boilerId;

    @NotNull
    @Column(name = "name", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @NotNull
    @Column(name = "article", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String article;

    @NotNull
    @Column(name = "old_article", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String oldArticle;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_name_set_history_id", nullable = false)
    private ComponentNameSetHistory componentNameSetHistory;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @NotNull
    @Column(name = "type_operation", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer typeOperation;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @JdbcTypeCode(SqlTypes.BIT)
    private Boolean isActive = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;
}