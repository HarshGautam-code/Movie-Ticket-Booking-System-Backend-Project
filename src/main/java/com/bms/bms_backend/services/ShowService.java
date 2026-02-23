package com.bms.bms_backend.services;

import com.bms.bms_backend.controllers.MailService;
import com.bms.bms_backend.dto.RegisterSeatBookingDto;
import com.bms.bms_backend.dto.RegisterShowDto;
import com.bms.bms_backend.dto.RegisterShowPriceMappingDto;
import com.bms.bms_backend.dto.SeatStatusDto;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.*;
import com.bms.bms_backend.repositories.BillRepository;
import com.bms.bms_backend.repositories.ShowPriceMappingRepository;
import com.bms.bms_backend.repositories.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ShowService {

    MovieService movieService;
    HallService hallService;
    UserService userService;
    ShowRepository showRepository;

    ShowPriceMappingRepository showPriceMappingRepository;

    SeatBookingService seatBookingService;

    BillRepository billRepository;

    MailService mailService;

    //  1-1-2015 00:00:00
    LocalDateTime worldStartTime = LocalDateTime.of(2015, 1, 1, 0, 0, 0);

    @Autowired
    public ShowService(MovieService movieService, HallService hallService, UserService userService, ShowRepository showRepository, ShowPriceMappingRepository showPriceMappingRepository, SeatBookingService seatBookingService, BillRepository billRepository, MailService mailService) {
        this.movieService = movieService;
        this.hallService = hallService;
        this.userService = userService;
        this.showRepository = showRepository;
        this.showPriceMappingRepository = showPriceMappingRepository;
        this.seatBookingService = seatBookingService;
        this.billRepository = billRepository;
        this.mailService = mailService;
    }

    public Show createShow(UUID movieId, UUID hallId, UUID userId, RegisterShowDto registerShowDto) {

        // Validate all the ids are correct or not
        User user = userService.isUserIdExist(userId);
        Movie movie = movieService.isMovieIdValid(movieId);
        Hall hall = hallService.isHallIdValid(hallId);

        if(user == null || movie == null || hall == null){
            throw new IllegalArgumentException("Invalid Id's Passed");
        }
        if(!user.getUserType().equals("THEATER_OWNER")){
            throw new UnAuthorizedException("User is not of type theater Owner");
        }
        if(!hall.getTheater().getTheaterOwner().getId().equals(userId)){
            throw new UnAuthorizedException("User is not allowed to create show in this hall");
        }

        Show show = new Show();
        show.setShowName(registerShowDto.getShowName());
        show.setHall(hall);
        show.setMovie(movie);
        show.setDisplayStartTime(registerShowDto.getDisplayStartTime());
        show.setDisplayEndTime(registerShowDto.getDisplayEndTime());
        show.setTotalRevenue(0.0);
        show.setTotalTicketSold(0);

        LocalDateTime showStartTime = registerShowDto.getDisplayStartTime();
        long showStartTimeInSecond = Duration.between(worldStartTime, showStartTime).getSeconds();
        LocalDateTime showEndTime = registerShowDto.getDisplayEndTime();
        long showEndTimeInSecond = Duration.between(worldStartTime, showEndTime).getSeconds();
        show.setStartTimeInSec(showStartTimeInSecond);
        show.setEndTimeInSec(showEndTimeInSecond);

        // Before Creating this show -> I want to check that is this show overlapping with any other show created for this hall

        // I want to get all the shows created for the hall
        List<Show> shows = showRepository.findByHall(hall);
        shows.add(show);
        Collections.sort(shows, (a, b) -> Math.toIntExact((a.getStartTimeInSec() - b.getStartTimeInSec())));

        Boolean isOverLapping = false;

        for (int i = 1; i < shows.size(); i++) {
            Show s1 = shows.get(i - 1);
            Show s2 = shows.get(i);
            if (s2.getStartTimeInSec() <= s1.getEndTimeInSec()) {
                isOverLapping = true;
                break;
            }

        }

        if(isOverLapping){
            throw new IllegalArgumentException("Overlapping start and end timing");
        }

        return showRepository.save(show);
    }



    // Creating Show Price mapping

    public List<ShowPriceMapping> createPriceMappings(UUID userId, List<RegisterShowPriceMappingDto> mappingDtos) {

        User theaterOwner = userService.isUserIdExist(userId);
        if(theaterOwner == null){
            // If theaterOwner is null that means we got invaild id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist");
        }

        if(!theaterOwner.getUserType().equals("THEATER_OWNER")){ // MOVIE_OWNER
            throw new UnAuthorizedException("User does not have permission to create theater");
        }

        List<ShowPriceMapping> priceMappings = new ArrayList<>();

        for(RegisterShowPriceMappingDto mappingDto : mappingDtos){
            UUID showId = mappingDto.getShowId();
            Show show = this.getShowById(showId);
            UUID hallId = mappingDto.getHallId();
            Hall hall = hallService.isHallIdValid(hallId);
            UUID hallRowMappingId = mappingDto.getHallRowMappingId();
            HallRowMapping hallRowMapping = hallService.getHallRowMappingById(hallRowMappingId);

            if(show == null || hall == null || hallRowMapping == null){
                throw new IllegalArgumentException("Invalid ids passed in request body");
            }

            ShowPriceMapping showPriceMapping = new ShowPriceMapping();
            showPriceMapping.setShow(show);
            showPriceMapping.setHall(hall);
            showPriceMapping.setHallRowMapping(hallRowMapping);
            showPriceMapping.setPrice(mappingDto.getPrice());
            priceMappings.add(showPriceMapping);
        }

        return showPriceMappingRepository.saveAll(priceMappings);
    }


    private Show getShowById(UUID showId) {
        return showRepository.findById(showId).orElse(null);
    }



    // Getting all Seats Status

    public List<SeatStatusDto> fetchAllSeatStatus(UUID showId) {

        Show show = this.getShowById(showId);
        Hall hall = show.getHall();
        List<HallRowMapping> rowMappings = hallService.getHallRowMappingByHall(hall);

        List<SeatStatusDto> seatDetails = new ArrayList<>();

        for(HallRowMapping hallRowMapping : rowMappings){
            String rowRange = hallRowMapping.getRowRange();
            String [] range = rowRange.split("-");
            char st = range[0].charAt(0);
            char en = range[1].charAt(0);
            int seatCount = hallRowMapping.getSeatCount();
            ShowPriceMapping priceMapping  = showPriceMappingRepository.getPriceMappingRecordByShowAndRowMapping(showId, hallRowMapping.getId());
            Double price = priceMapping.getPrice();

            for(char row = st; row <= en; row++){
                for(int i = 1; i <= seatCount; i++){
                    String seatId = row + "-" + i;

                    // I want to check that this seat is already booked or not ?
                    boolean status = seatBookingService.isSeatAvailableForShow(seatId, show.getId());

                    SeatStatusDto seatStatusDto = new SeatStatusDto();
                    seatStatusDto.setStatus(status);
                    seatStatusDto.setShowId(showId);
                    seatStatusDto.setSeatType(hallRowMapping.getRowType());
                    seatStatusDto.setPrice(price);
                    seatStatusDto.setHallId(hall.getId());
                    seatStatusDto.setSeatId(seatId);

                    seatDetails.add(seatStatusDto);
                }
            }
        }

        return seatDetails;
    }



    //  Booking Seat and Getting bill

    public Bill bookSeatForShow(UUID showId, UUID userId, RegisterSeatBookingDto seatBookingDto) {

        Show show = this.getShowById(showId);
        User customer = userService.isUserIdExist(userId);

        if(show == null || customer == null){
            throw  new IllegalArgumentException("Invalid Id's is Passed");
        }


        List<String> seatIds = seatBookingDto.getSeatIds();

        for(String seatId : seatIds){
            boolean seatStatus = seatBookingService.isSeatAvailableForShow(seatId, showId);

            if(!seatStatus){
                throw new IllegalArgumentException("Seat is already booked " + seatId);
            }
        }


        List<SeatBooking> seatbookingList = new ArrayList<>();
        double price = 0;

        for(String seatId : seatIds){

            ShowPriceMapping showPriceMapping = this.getShowPriceOnTheBasisOfSeatId(seatId, show);
            price += showPriceMapping.getPrice();

            SeatBooking seatBooking = new SeatBooking();
            seatBooking.setShow(show);
            seatBooking.setSeatId(seatId);
            seatBooking.setUser(customer);

            String ticketId = seatBookingService.generateTicketId();
            seatBooking.setTicketId(ticketId);

            seatBooking.setTicketId(ticketId);
            seatBooking = seatBookingService.saveOrUpdate(seatBooking);
            seatbookingList.add(seatBooking);
        }


        Bill bill = new Bill();
        bill.setSeatBookings(seatbookingList);
        bill.setTheater(show.getHall().getTheater());
        bill.setCustomer(customer);
        bill.setPaymentId(seatBookingDto.getPaymentId());
        bill.setPaymentSource(seatBookingDto.getPaymentSource());
        bill.setTotalPrice(price);



        // Save this bill in the bill table

        mailService.sendBillDetailsToCustomer(bill);
        return billRepository.save(bill);
    }


    public ShowPriceMapping  getShowPriceOnTheBasisOfSeatId(String seatId, Show show){
        HallRowMapping hallRowMapping = this.getRowMappingOnTheBasisOfSeatId(seatId, show.getHall());
        return showPriceMappingRepository.getPriceMappingRecordByShowAndRowMapping(show.getId(), hallRowMapping.getId());
    }


    // A-12
    private HallRowMapping getRowMappingOnTheBasisOfSeatId(String seatId, Hall hall) {
        // A-X -> Z-12
       List<HallRowMapping> rowMappings = hallService.getHallRowMappingByHall(hall);

        // [A, 12]
        char seatRow = seatId.split("-")[0].charAt(0);
        for(HallRowMapping rowMapping : rowMappings){
            String range = rowMapping.getRowRange(); // A-D
            String [] rangeArr = range.split("-"); // [A, D]
            char stRange = rangeArr[0].charAt(0); // A
            char enRange = rangeArr[1].charAt(0); // D

            // A>=A && A<=D
            if(seatRow >= stRange && seatRow <= enRange){
                return rowMapping;
            }
        }

        return null;
    }


}
