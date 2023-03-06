package com.codestates.question.entity;


import com.codestates.audit.Auditable;
import com.codestates.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question extends Auditable {


}
