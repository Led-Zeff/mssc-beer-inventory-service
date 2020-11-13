package courses.microservices.beerservice.event;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewInventoryEvent implements Serializable {

  private static final long serialVersionUID = 4465634783752679581L;
  
  private BeerDto beerDto;
  
}
