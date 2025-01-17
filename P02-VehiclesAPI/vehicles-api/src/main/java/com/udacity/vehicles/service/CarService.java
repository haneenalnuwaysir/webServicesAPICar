package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final MapsClient mapsClient;
    private final PriceClient priceClient;
    public CarService(CarRepository repository, WebClient pricing, WebClient maps, ModelMapper modelMapper) {
        this.mapsClient = new MapsClient(maps, modelMapper);
        this.priceClient = new PriceClient(pricing);
        this.repository = repository;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setPrice(priceClient.getPrice(id));
        car.setLocation(mapsClient.getAddress(car.getLocation()));
        return car;
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
//        Car car = new Car();

        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
//        Location mapLocation = mapsClient.getAddress(car.getLocation());
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
//    public Car save(Car car) {
//        if (car.getId() != null) {
//            return repository.findById(car.getId())
//                    .map(carToBeUpdated -> {
//                        carToBeUpdated.setDetails(car.getDetails());
//                        carToBeUpdated.setLocation(car.getLocation());
//                        return repository.save(carToBeUpdated);
//                    }).orElseThrow(CarNotFoundException::new);
//        }
//
//        return repository.save(car);
//    }
    public Car save(Car car) {
        car.setCreatedAt(LocalDateTime.now());
//        car.setCondition(Condition.NEW);
        return repository.save(car);
    }

    public Car update(Car car) {
        if (repository.findById(car.getId()).isPresent()) {
            car.setModifiedAt(LocalDateTime.now());
//            car.setCondition(Condition.USED);
            return repository.save(car);
        } else {
            throw new CarNotFoundException(car.getId());
        }
    }


    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        repository.delete(repository.findById(id).orElseThrow(CarNotFoundException::new));
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        /**
         * TODO: Delete the car from the repository.
         */

    }
}