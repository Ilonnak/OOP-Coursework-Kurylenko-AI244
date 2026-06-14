package ua.opnu.labwork2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private Integer capacity;
    private String manufacturer;

    @OneToMany(mappedBy = "aircraft")
    private List<Flight> flights;

    public Aircraft() {
    }

    public Aircraft(Long id, String model, Integer capacity, String manufacturer) {
        this.id = id;
        this.model = model;
        this.capacity = capacity;
        this.manufacturer = manufacturer;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public Integer getCapacity() { return capacity; }

    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getManufacturer() { return manufacturer; }

    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public List<Flight> getFlights() { return flights; }

    public void setFlights(List<Flight> flights) { this.flights = flights; }
}