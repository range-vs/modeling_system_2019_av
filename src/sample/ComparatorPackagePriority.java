package sample;

import java.util.Comparator;

public class ComparatorPackagePriority implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return (((WorkStationPackage)o1).getPriority().ordinal() - ((WorkStationPackage)o2).getPriority().ordinal());
    }
}
