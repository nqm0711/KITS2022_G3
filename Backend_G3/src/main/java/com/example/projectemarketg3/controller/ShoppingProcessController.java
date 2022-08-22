package com.example.projectemarketg3.controller;

import com.example.projectemarketg3.dto.DetailDto;
import com.example.projectemarketg3.dto.InfoUserShoppingDto;
import com.example.projectemarketg3.dto.RatingDto;
import com.example.projectemarketg3.dto.UserIdDto;
import com.example.projectemarketg3.entity.*;
import com.example.projectemarketg3.repository.*;
import com.example.projectemarketg3.request.StatusOrderRequest;
import com.example.projectemarketg3.service.OrderService;
import com.example.projectemarketg3.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ShoppingProcessController {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RankingRepository rankingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ShoppingService shoppingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartItemRepository cartItemRepository;


    //    ADD CART
    @PostMapping("/carts")
    public CartItem createOrderDetails(@RequestBody DetailDto detailDto) {
        return shoppingService.clickAddCart(detailDto);
    }

    //    XEM GIO HANG theo id khach
    @GetMapping("/carts")
    public List<DetailDto> cartByUserId(@RequestBody UserIdDto id) {
//        lay ra danh sach san pham co trong gio hang o detail theo id khach
//        Set<OrderDetail> orderDetails = orderDetailRepository.findByUser_Id(id.getId());
        List<CartItem> cartItems = cartItemRepository.getByUser_Id(id.getId());

//        tra ve data theo DetailDto
//        List<DetailDto> detailDtos = new ArrayList<>();
//        orderDetails.forEach(s -> {
//            detailDtos.add(orderService.getOrderDetail(s));
//        });

        List<DetailDto> detailDtos = new ArrayList<>();
        cartItems.forEach(s -> {
            detailDtos.add(orderService.getOrderDetail(s));
        });
        return detailDtos;
    }


    //    CLICK BUY -> add BILL(createAt,idDetail,idUser,ship,totalPrice) PUT-> quantity product
    @PostMapping("/order-bill")
    public Orders clickBuy(@RequestBody InfoUserShoppingDto info) {
        return shoppingService.clickBuy(info);
    }

    //    CLICK HUY DON
    @PutMapping("/order-bill")
    public Orders clickCancelOrder(@PathVariable Long id) {
        return shoppingService.clickCancelOrder(id);
    }

    //    CLICK TANG/GIAM SO LUONG SAN PHAM (-a -> a)
    @PutMapping("/quantity-detail/{id}")
    public CartItem clickUpdateQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        Optional<CartItem> orderDetail = cartItemRepository.findById(id);
        orderDetail.get().setQuantity(orderDetail.get().getQuantity() + quantity);
        return cartItemRepository.save(orderDetail.get());
    }

    //CLICK XOA SAN PHAM TRONG CART
    @DeleteMapping("/detail-delete/{id}")
    public void clickDeleteOrderDetail(@PathVariable Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        cartItem.ifPresent(item -> cartItemRepository.delete(item));
    }

    //    UPDATE STATUS ORDER
    @PutMapping("/update-status-order")
    public Orders clickUpdateOrderStaus(@RequestBody StatusOrderRequest request) {
        Optional<Orders> orders = ordersRepository.findById(request.getOrderId());
        Optional<Status> status = statusRepository.findById(request.getStatusId());
        Optional<User> user = userRepository.findById(request.getUserId());

        orders.get().setStatus(status.get());
        orders.get().setUserSucceed(user.get());

        return ordersRepository.save(orders.get());
    }

    //  NEW DATA RATING ->  DANH GIA DON HANG CHECKING = 0;
    @PostMapping("/rating-product")
    public Rating ratingProduct(@RequestBody RatingDto ratingDto) {
        return shoppingService.ratingProduct(ratingDto);
    }

    //    XAC NHAN DANH GIA CHECKING = true -> UPDATE AVG_RATING(product)
    @PutMapping("/rating-product")
    public Rating updateCheckingProduct(@RequestBody UserIdDto id) {
        return shoppingService.updateCheckingProduct(id);
    }

    //    LAY RA DANH GIA CUA SAN PHAM THEO ID VA DA DUOC ADMIN PUBLIC
    @GetMapping("/rating-product/{id}")
    public List<Rating> getAllRatingProduct(@PathVariable Long id) {
        List<Rating> ratings = ratingRepository.findByProduct_IdOrderByCreateAtDesc(id);
        List<Rating> ratingList = new ArrayList<>();
        for (Rating r : ratings
        ) {
            if (r.getChecking()) {
                ratingList.add(r);
            }
        }
        return ratingList;
    }

    //    LAY RA DANH GIA CUA SAN PHAM THEO USER
    @GetMapping("/rating-user/{id}")
    public List<Rating> getAllRatingUser(@PathVariable Long id) {
        return ratingRepository.findByUser_IdOrderByCreateAtDesc(id);
    }

//    RESET RANK 6 THANG 1 LAN

}
