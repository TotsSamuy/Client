package data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Ticket implements Comparable<Ticket>, Serializable {
    private static final long serialVersionUID = 1L;

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой

    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float price; //Значение поля должно быть больше 0
    private long discount; //Значение поля должно быть больше 0, Максимальное значение поля: 100
    private String comment; //Строка не может быть пустой, Поле не может быть null

    private TicketType type; //Поле может быть null
    private String user;
    private Integer eventId; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    public Integer getEventId() {
        if (event != null) {
            return event.getId();
        }
        return eventId;
    }

    public String getEventName() {
        if (event != null) {
            return event.getName();
        }
        return eventName;
    }

    public Integer getMinAge() {
        if (event != null) {
            return event.getMinAge();
        }
        return minAge;
    }

    public Integer getTicketCount() {
        if (event != null) {
            return event.getTicketsCount();
        }
        return ticketCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && Float.compare(ticket.price, price) == 0 && discount == ticket.discount && name.equals(ticket.name) && creationDate.equals(ticket.creationDate) && comment.equals(ticket.comment) && type == ticket.type && coordinates.equals(ticket.coordinates);
    }

    private String eventName; //Поле не может быть null, Строка не может быть пустой
    private Integer minAge;
    private Integer ticketCount;
    private Coordinates coordinates; //Поле не может быть null
    private Event event; //Поле может быть null

    public String getUser() {
        return user;
    }

    public Ticket(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, float price, long discount, String comment, TicketType type, Event event, String user) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.comment = comment;
        this.type = type;
        this.event = event;
        this.user = user;
//        this.x = coordinates.getX();
//        this.y = coordinates.getY();
//        this.eventName = event.getName();
//        this.eventId = event.getId();
//        this.ticketsCount = event.getTicketsCount();
//        this.minAge = event.getMinAge();
    }

    public double getX() {
        return coordinates.getX();
    }

    public float getY() {
        return coordinates.getY();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Override
    public String toString() {
        return "data.Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", discount=" + discount +
                ", comment='" + comment + '\'' +
                ", type=" + type +
                ", event=" + event +
                '}';
    }

    public String toCsv() {
        StringBuilder s = new StringBuilder();
        s.append(name + "," + coordinates.toCsv() + "," + price + "," + discount + "," + comment);
        if (type != null) {
            s.append("," + type.toCsv());
        }
        if (event != null) {
            s.append("," + event.toCsv());
        }

        return s.toString();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, discount, comment, type, event);
    }

    @Override
    public int compareTo(Ticket o) {
        if (this.getPrice() - o.getPrice() > 0) {
            return 1;
        }

        if (this.getPrice() - o.getPrice() < 0) {
            return -1;
        }

        return this.getId() - o.getId();
    }
}
