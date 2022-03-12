package com.berkanterdogan.microservices.lab.kafka.model;

import com.berkanterdogan.microservices.lab.kafka.model.base.BaseKafkaMessageModelDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterKafkaMessageModelDto extends BaseKafkaMessageModelDto {

   private long userId;
   private long id;
   private String text;
   private Long createdAt;

  public TwitterKafkaMessageModelDto() {}

  public TwitterKafkaMessageModelDto(Long userId, Long id, String text, Long createdAt) {
    this.userId = userId;
    this.id = id;
    this.text = text;
    this.createdAt = createdAt;
  }
}










