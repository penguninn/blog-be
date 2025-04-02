package com.daviddai.blog.model.entities;

import com.daviddai.blog.model.enums.EToken;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token extends AbstractEntity {

    private String refreshToken;

    @Builder.Default
    private EToken tokenType = EToken.BEARER;

    private boolean revoked;

    private boolean expried;

}
