package io.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.monitoring.model.dict.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "SERVICE")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ulr")
    private String url;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonProperty("created")
    @Column(name = "created")
    private Date creationDate = Date.from(Instant.now(Clock.systemDefaultZone()));

    @JsonProperty("updated")
    @Column(name = "updated")
    private Date updateDate = Date.from(Instant.now(Clock.systemDefaultZone()));

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ServiceStatus status;
}
