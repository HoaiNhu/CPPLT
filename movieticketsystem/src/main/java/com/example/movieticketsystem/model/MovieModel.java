// package com.example.movieticketsystem.model;

// public class MovieModel {
//     private int id;
//     private String title;
//     private String genre;
//     private double ticketPrice;

//     public MovieModel() {
//     }

//     public MovieModel(String title, String genre, double ticketPrice) {
//         this.title = title;
//         this.genre = genre;
//         this.ticketPrice = ticketPrice;
//     }

//     // Getter và Setter cho id
//     public int getId() {
//         return id;
//     }

//     public void setId(int id) {
//         this.id = id;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public String getGenre() {
//         return genre;
//     }

//     public void setGenre(String genre) {
//         this.genre = genre;
//     }

//     public double getTicketPrice() {
//         return ticketPrice;
//     }

//     public void setTicketPrice(double ticketPrice) {
//         this.ticketPrice = ticketPrice;
//     }
// }

package com.example.movieticketsystem.model;

public class MovieModel {
    private int id;
    private String title;
    private String genre;
    private double ticketPrice;

    public MovieModel() {
        // Khởi tạo giá trị mặc định
        this.id = 0;
        this.title = "";
        this.genre = "";
        this.ticketPrice = 0.0;
    }

    public MovieModel(String title, String genre, double ticketPrice) {
        this.id = 0; // ID mặc định là 0 để biểu thị đây là bản ghi mới
        this.title = title;
        this.genre = genre;
        this.ticketPrice = ticketPrice;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre != null ? genre : "";
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    
    @Override
    public String toString() {
        return "MovieModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}