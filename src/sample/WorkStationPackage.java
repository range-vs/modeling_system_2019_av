package sample;

public class WorkStationPackage
{
    private double timeAdd; // time add package
    private int countBits; // count bits
    private PriorityValue priority; // priority
    private int timeBit;  // time on send one bit

    private int timeBitTmp; // временное время на один бит, служит для того, чтобы не потерять время передачи одного бита
    private boolean isSend; // флаг, передан пакет или нет

    public void SendBit() // передачи нужного количества бит
    {
        timeBitTmp += 100; // инкрементируем времся на 100 нс
        if (timeBitTmp == timeBit) // если мы достигли времени передачи бита
        {
            timeBitTmp = 0; // обнуляем его
            countBits--; // и передаём бит
        }
        if (countBits == 0) // биты закончились
        {
            isSend = true;  // пакет передан
        }
    }

    public WorkStationPackage() {
        this.timeBitTmp = 0;
        this.isSend = false;
    }

    public WorkStationPackage(boolean iss) {
        this.timeBitTmp = 0;
        this.isSend = iss;
    }

    public double getTimeAdd() {
        return timeAdd;
    }

    public void setTimeAdd(double timeAdd) {
        this.timeAdd = timeAdd;
    }

    public int getCountBits() {
        return countBits;
    }

    public void setCountBits(int countBits) {
        this.countBits = countBits;
    }

    public PriorityValue getPriority() {
        return priority;
    }

    public void setPriority(PriorityValue priority) {
        this.priority = priority;
    }

    public int getTimeBit() {
        return timeBit;
    }

    public void setTimeBit(int timeBit) {
        this.timeBit = timeBit;
    }

    public int getTimeBitTmp() {
        return timeBitTmp;
    }

    public void setTimeBitTmp(int timeBitTmp) {
        this.timeBitTmp = timeBitTmp;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}