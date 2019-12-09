package sample;

public class OptionData {
    private int countWorkStation;// кол -в о раб станций
    private int sredCountPackageOnOneWorkStatinon;// среднее клдичество пакетов в единицу времени отдающееся одной раб станцией
    private Pair<Integer, Integer> barriedGenerationCountBits;// границы генерации кол-ва бит(первое число - мин значение, второе число - макс значение)
    private Pair<Integer, Integer> barriedGenerationTimeBit;// тоже самое, что и выше, только для времени передачи 1 бита
    private Priority generatorPriority;// генератор приоритетов
    private int countChannels;// кол - во каналов
    private int countWorkingSeconds;// количество секунд работы системы
    private double countLoopSeconds;// количество секунд обновления системы(вывода данных на график и тд)

    public OptionData(int countWorkStation, int sredCountPackageOnOneWorkStatinon, int barriedGenerationCountBitsL, int barriedGenerationCountBitsR,
                      int barriedGenerationTimeBitL, int barriedGenerationTimeBitR, double pl, double pm, double ph,
                      int countChannels, int countWorkingSeconds, double countLoopSeconds) {
        this.countWorkStation = countWorkStation;
        this.sredCountPackageOnOneWorkStatinon = sredCountPackageOnOneWorkStatinon;
        this.barriedGenerationCountBits = new Pair<>(barriedGenerationCountBitsL, barriedGenerationCountBitsR);
        this.barriedGenerationTimeBit = new Pair<>(barriedGenerationTimeBitL, barriedGenerationTimeBitR);
        this.generatorPriority = new Priority(pl, pm, ph);// генератор принимает три вероятности приоритета, по ним будет рассчитывается итоговый приоритет
        this.countChannels = countChannels;
        this.countWorkingSeconds = countWorkingSeconds;
        this.countLoopSeconds = countLoopSeconds;
    }

    public int getCountWorkStation() {
        return countWorkStation;
    }

    public int getSredCountPackageOnOneWorkStatinon() {
        return sredCountPackageOnOneWorkStatinon;
    }

    public Pair<Integer, Integer> getBarriedGenerationCountBits() {
        return barriedGenerationCountBits;
    }

    public Pair<Integer, Integer> getBarriedGenerationTimeBit() {
        return barriedGenerationTimeBit;
    }

    public Priority getGeneratorPriority() {
        return generatorPriority;
    }

    public int getCountChannels() {
        return countChannels;
    }

    public int getCountWorkingSeconds() {
        return countWorkingSeconds;
    }

    public double getCountLoopSeconds() {
        return countLoopSeconds;
    }
}
