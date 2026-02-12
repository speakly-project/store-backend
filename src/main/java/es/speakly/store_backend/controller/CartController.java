package es.speakly.store_backend.controller;


import es.speakly.store_backend.controller.webmodel.request.CartPaymentRequest;
import es.speakly.store_backend.controller.webmodel.request.OrderUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.OrderResponse;
import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.service.CartService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.mappers.OrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/speakly/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> getCart(@PathVariable Long id){
        OrderDto orderDto = cartService.getCart(id);
        OrderResponse orderResponse = OrderMapper.fromOrderDtoToOrderResponse(orderDto);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody OrderUpdateRequest orderUpdateRequest){
        UserDto userDto = userService.getById(orderUpdateRequest.userId());

        OrderDto orderDto = OrderMapper.fromOrderUpdateRequestToOrderDto(orderUpdateRequest, userDto);
        cartService.updatePendingCart(orderDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("pay")
    public ResponseEntity<Void> payCart(@RequestBody CartPaymentRequest cartPaymentRequest) {
        DtoValidator.validate(cartPaymentRequest);

        cartService.payCart(
                cartPaymentRequest.userId(),
                cartPaymentRequest.cardNumber(),
                cartPaymentRequest.expiryDate(),
                cartPaymentRequest.cvv(),
                cartPaymentRequest.fullName()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
