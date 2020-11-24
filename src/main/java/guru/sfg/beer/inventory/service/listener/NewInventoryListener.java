package guru.sfg.beer.inventory.service.listener;

import javax.transaction.Transactional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import courses.microservices.brewery.event.NewInventoryEvent;
import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewInventoryListener {

  private final BeerInventoryRepository beerInventoryRepository;
  
  @Transactional
  @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
  public void listen(NewInventoryEvent newInventoryEvent) {
    log.info("New inventory order. Quantity: {}, for beer: {}", newInventoryEvent.getBeerDto().getQuantityOnHand());
    beerInventoryRepository.save(BeerInventory.builder()
      .beerId(newInventoryEvent.getBeerDto().getId())
      .upc(newInventoryEvent.getBeerDto().getUpc())
      .quantityOnHand(newInventoryEvent.getBeerDto().getQuantityOnHand())
      .build()
    );
  }

}
