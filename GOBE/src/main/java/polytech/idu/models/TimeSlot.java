package polytech.idu.models;

import polytech.idu.models.enums.TimeSlotStatus;

import java.util.Date;

public class TimeSlot {
    protected Profile profile;
    protected Advertisement advertisement;
    protected float ammount;
    protected Date date;
    protected TimeSlotStatus status;
}
