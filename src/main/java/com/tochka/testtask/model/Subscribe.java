package com.tochka.testtask.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscribe
{
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String sourceURL;
	@Column
	private String newsPath;
	@Column
	private String titlePath;
	@Column
	private String descriptionPath;
	@Column
	private ParseType type;

	@OneToMany(mappedBy = "sourceSubscribe")
	private List<News> news;


}
