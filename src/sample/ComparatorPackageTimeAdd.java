package sample;

import java.util.Comparator;

public class ComparatorPackageTimeAdd implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return (int)(((WorkStationPackage)o1).getTimeAdd() - ((WorkStationPackage)o2).getTimeAdd());
    }
}
