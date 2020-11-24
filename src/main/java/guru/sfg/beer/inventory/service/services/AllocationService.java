package guru.sfg.beer.inventory.service.services;

import courses.microservices.brewery.model.BeerOrderDto;

public interface AllocationService {
  Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
