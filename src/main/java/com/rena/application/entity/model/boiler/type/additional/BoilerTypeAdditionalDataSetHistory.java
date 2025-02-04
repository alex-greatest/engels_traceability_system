package com.rena.application.entity.model.boiler.type.additional;

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
@Table(name = "boiler_type_additional_data_set_history", indexes = {
        @Index(name = "idx_boiler_type_additional_data_set_history_name_is_active", columnList = "name, is_active"),
        @Index(name = "idx_boiler_type_additional_data_set_history_data_set_id", columnList = "boiler_type_addition_data_set_id")
})
public class BoilerTypeAdditionalDataSetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_type_addition_data_set_id", nullable = false)
    private Long boilerTypeAdditionDataSetId;

    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @Column(name = "old_name", length = 100)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String oldName;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;
}