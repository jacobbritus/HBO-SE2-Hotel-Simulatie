package events;

public class HotelEvent {
    private  int time;
    private  HotelEventType eventType;
    private  Integer humanId;
    private  int data;

    public HotelEvent(HotelEventType eventType, int time, Integer humanId, int data) {
        this.eventType = eventType;
        this.time = time;
        this.humanId = humanId;
        this.data = data;
    }

    public HotelEventType getEventType() {
        return this.eventType;
    }

    public Integer getHumanId() {
        return this.humanId;
    }

    public void setHumanId(Integer val) {
        this.humanId = val;
    }

    public int getData() {
        return this.data;
    }

    public void setData(Integer val) {
        this.data = val;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(Integer val) {
        this.time = val;
    }
}
