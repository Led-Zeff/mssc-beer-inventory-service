package guru.sfg.beer.inventory.service.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import courses.microservices.brewery.model.BeerOrderDto;
import courses.microservices.brewery.model.BeerOrderLineDto;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {
  
  private final BeerInventoryRepository beerInventoryRepository;

  @Override
  public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
    log.debug("Allocating order");

    AtomicInteger totalOrdered = new AtomicInteger();
    AtomicInteger totalAllocated = new AtomicInteger();

    beerOrderDto.getBeerOrderLines().forEach(beerOrderLine -> {
      if (Optional.of(beerOrderLine.getOrderQuantity()).orElse(0) - Optional.of(beerOrderLine.getQuantityAllocated()).orElse(0) > 0) {
        allocateBeerOrderLine(beerOrderLine);
      }
      totalOrdered.addAndGet(beerOrderLine.getOrderQuantity());
      totalAllocated.addAndGet(beerOrderLine.getQuantityAllocated());
    });

    log.debug("Total ordered: {}. Total allocated {}", totalOrdered.get(), totalAllocated.get());
    return totalOrdered.get() == totalAllocated.get();
  }

  private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLineDto) {
    List<BeerInventory> beerInventorieList = beerInventoryRepository.findAllByUpc(beerOrderLineDto.getUpc());

    beerInventorieList.forEach(beerInventory -> {
      int inventory = Optional.of(beerInventory.getQuantityOnHand()).orElse(0);
      int orderQty = Optional.of(beerOrderLineDto.getOrderQuantity()).orElse(0);
      int allocatedQty = Optional.of(beerOrderLineDto.getQuantityAllocated()).orElse(0);
      int qtyToAllocate = orderQty - allocatedQty;

      if (inventory >= qtyToAllocate) {
        inventory -= qtyToAllocate;
        beerOrderLineDto.setQuantityAllocated(orderQty);
        beerInventory.setQuantityOnHand(inventory);

        beerInventoryRepository.save(beerInventory);
      } else {
        beerOrderLineDto.setQuantityAllocated(allocatedQty + inventory);
        beerInventory.setQuantityOnHand(0);

        beerInventoryRepository.delete(beerInventory);
      }
    });
  }

}
