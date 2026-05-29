hotel-management-app/
│
├── src/main/java/com/hotel/
│
│   ├── app/
│   │   └── MainApp.java
│   │
│   ├── domain/                      <-- PURE business entities
│   │   ├── Guest.java
│   │   ├── Room.java
│   │   ├── Booking.java
│   │   └── Admin.java
│   │
│   ├── repository/                 <-- DB layer (interfaces + impl)
│   │   ├── GuestRepository.java
│   │   ├── impl/
│   │   │   ├── GuestRepositoryImpl.java
│   │   │   └── RoomRepositoryImpl.java
│   │
│   ├── service/                   <-- business logic
│   │   ├── GuestService.java
│   │   ├── BookingService.java
│   │   └── RoomService.java
│   │
│   ├── ui/                        <-- JavaFX layer
│   │   ├── controller/
│   │   │   ├── GuestController.java
│   │   │   ├── RoomController.java
│   │   │   └── BookingController.java
│   │   │
│   │   ├── viewmodel/            <-- JavaFX properties (important!)
│   │   │   ├── GuestViewModel.java
│   │   │   └── BookingViewModel.java
│   │   │
│   │   └── view/
│   │       ├── GuestView.fxml
│   │       ├── RoomView.fxml
│   │       └── BookingView.fxml
│   │
│   ├── infrastructure/          <-- technical stuff
│   │   ├── database/
│   │   │   ├── DatabaseConfig.java
│   │   │   └── ConnectionPool.java
│   │   │
│   │   └── mapper/
│   │       ├── GuestMapper.java
│   │       └── BookingMapper.java
│   │
│   └── util/
│       ├── DateUtil.java
│       └── ValidationUtil.java
│
└── src/main/resources/
├── css/
├── images/
└── fxml/

mozda svicat na microservice arh
user -> userrepo, userservice,
booking -> itd...