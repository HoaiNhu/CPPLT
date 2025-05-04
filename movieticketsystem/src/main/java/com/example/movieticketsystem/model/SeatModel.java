// package com.example.movieticketsystem.model;

// public class SeatModel {
//     private int id;
//     private String seatRow;
//     private int seatCol;
//     private String seatStatus;
//     private String customerName;
//     private String phone;
//     private String email;

//     public SeatModel(int id, String seatRow, int seatCol, String seatStatus) {
//         this.id = id;
//         this.seatRow = seatRow;
//         this.seatCol = seatCol;
//         this.seatStatus = seatStatus;
//     }

//     public int getId() { return id; }
//     public void setId(int id) { this.id = id; }
//     public String getSeatRow() { return seatRow; }
//     public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
//     public int getSeatCol() { return seatCol; }
//     public void setSeatCol(int seatCol) { this.seatCol = seatCol; }
//     public String getSeatStatus() { return seatStatus; }
//     public void setSeatStatus(String seatStatus) { this.seatStatus = seatStatus; }
//     public String getCustomerName() { return customerName; }
//     public void setCustomerName(String customerName) { this.customerName = customerName; }
//     public String getPhone() { return phone; }
//     public void setPhone(String phone) { this.phone = phone; }
//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }
// }

package com.example.movieticketsystem.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class SeatModel {
    private int id;
    private String seatRow;
    private int seatCol;
    private AtomicBoolean available; // Thay seatStatus bằng AtomicBoolean
    private String customerName;
    private String phone;
    private String email;
    private int theaterId; // Thêm trường này

    public SeatModel(int id, String seatRow, int seatCol, String seatStatus, int theaterId) {
        this.id = id;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.available = new AtomicBoolean("available".equals(seatStatus));
        this.theaterId = theaterId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
    public int getSeatCol() { return seatCol; }
    public void setSeatCol(int seatCol) { this.seatCol = seatCol; }
    public String getSeatStatus() { return available.get() ? "available" : "booked"; }
    public void setSeatStatus(String seatStatus) { this.available.set("available".equals(seatStatus)); }
    public AtomicBoolean getAvailable() { return available; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }
    public String getSeatCode() {
        return seatRow + seatCol;
    }
}