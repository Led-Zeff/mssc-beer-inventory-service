package guru.sfg.beer.inventory.service.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import courses.microservices.brewery.model.AllocateOrderRequest;
import courses.microservices.brewery.model.AllocationOrderResult;
import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationRequestListener {
  
  private final JmsTemplate jmsTemplate;
  private final AllocationService allocationService;

  @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
  public void listen(AllocateOrderRequest allocateOrderRequest) {
    AllocationOrderResult.AllocationOrderResultBuilder resultBuilder = AllocationOrderResult.builder();
    resultBuilder.beerOrderDto(allocateOrderRequest.getBeerOrderDto());

    try {
      resultBuilder.pendingInventory(allocationService.allocateOrder(allocateOrderRequest.getBeerOrderDto()));
    } catch (Exception e) {
      resultBuilder.allocationError(true);
    }
    jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE, resultBuilder.build());

    log.debug("Allocate order {} request processed", allocateOrderRequest.getBeerOrderDto().getId());
  }

}
