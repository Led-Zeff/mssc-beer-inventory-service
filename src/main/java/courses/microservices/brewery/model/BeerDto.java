package courses.microservices.brewery.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BeerDto implements Serializable {
  private static final long serialVersionUID = 3198785099287357898L;

  @Null
  private UUID id;

  @Null
  private Integer version;

  @Null
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = Shape.STRING)
  private OffsetDateTime createdDate;
  
  @Null
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = Shape.STRING)
  private OffsetDateTime lastModifiedDate;

  @NotBlank
  private String beerName;

  @NotNull
  private BeerStyle beerStyle;

  @NotNull
  private String upc;

  @Positive
  @NotNull
  @JsonFormat(shape = Shape.STRING)
  private BigDecimal price;
  
  private Integer quantityOnHand;

}