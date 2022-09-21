
package com.fdcorp.blogposts.model;

/*
 * Copyright (c) 2022 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Entity  @Data  @NoArgsConstructor  @AllArgsConstructor  @Builder
public class Subtopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subtopicId;
    @NotBlank(message = "Community name is required")
    private String subtopicName;
    @NotBlank(message = "Description is required")
    private String subtopicDescription;
    @OneToOne(fetch = FetchType.LAZY)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
