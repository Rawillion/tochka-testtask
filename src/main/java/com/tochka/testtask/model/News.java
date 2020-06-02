package com.tochka.testtask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class News
{
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String title;

	@Column(columnDefinition = "text")
	private String description;

	@ManyToOne
	@JoinColumn(name = "source_subscribe_id")
	@JsonIgnore
	private Subscribe sourceSubscribe;
}
