package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.math.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Controller {

    @FXML
    private LineChart graphic;

    private XYChart.Series packageQueue = new XYChart.Series();
    private XYChart.Series packageIsWorked = new XYChart.Series();
    private ObservableList<XYChart.Data> packageQueueData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data> packageIsWorkedData = FXCollections.observableArrayList();

    private double countCurrentSecondsGui;
    private long countSendsPackageGui;

    private Thread smo;

    @FXML
    public void initialize() {
        packageQueue.setName("Пакетов в очереди");
        packageIsWorked.setName("Пакетов обработано");
        packageQueueData.add(new XYChart.Data(0, 0));
        packageIsWorkedData.add(new XYChart.Data(0, 0));
        packageQueue.setData(packageQueueData);
        packageIsWorked.setData(packageIsWorkedData);
        graphic.getData().add(packageQueue);
        graphic.getData().add(packageIsWorked);

        Runnable r = ()->{
            startModeling();
        };
        smo = new Thread(r,"modelingSMO");
        smo.start();
    }


    public void startModeling() {
        // создаём условие
        double iteration = 0.5; // время одной итерации в сек(генерация пакетов и обработка пакетов)
        int timeModeling = 10; // общее время моделирования в сек
        double countCurrentSeconds = 0; // текущее количество секунд
        // колво раб станций, сред колво пакетов, мин бит, макс бит, мин время бит, макс вреся бит, приоритет низ, сред, выс, колво каналов, время моделир(сек),
        // время обновления
        OptionData optionData = new OptionData(141, 15, 128, 256, 1, 4, 0.2, 0.6, 0.2, 4, timeModeling, iteration);
        BigInteger countIterationModeling = BigInteger.valueOf((long)(iteration * 1000000000L)); // 0.5с в нс
        System.out.println("Время моделирования системы: " + timeModeling + "s");

        // создаем рандомайзеры
        Random randomTimeAdd = new Random(); // для времени прихода
        Random randomCountBits = new Random(); // количество бит
        Random randomTimeBit = new Random(); // время предачи одного бита
        Random randomPriority = new Random(); // приоритет

        // создаём доп переменные
        long countSendsPackage = 0; // количество переданных пакетов
        Queue<WorkStationPackage> queuePackagePriority = new PriorityQueue<>(new ComparatorPackagePriority()); // создаём приоритетную очередь пакетов с автосортировкой по приоитету
        BigInteger allTime = BigInteger.valueOf(0); // общее время, нс, будет накапливаться
        BigInteger iterationTime = BigInteger.valueOf(0); // время итерации, нс, будет накапливаться
        BigInteger allPackage = BigInteger.valueOf(0); // общее количество пакетов, будет накапливаться

        // создаём узел с каналами
        ArrayList<WorkStationPackage> channels = new ArrayList<WorkStationPackage>(); // массив с каналами
        for (int i = 0; i < optionData.getCountChannels(); ++i)
        {
            channels.add(new WorkStationPackage(true)); // помещаем в него пустые пакеты( по умолчанию)
        }

        // начинаем моделирование
        while (countCurrentSeconds < timeModeling) // пока текущее кол - во секунд меньше времени моделирования
        {
            // тут генерируются заявки - {iteration} сек
            double time = 0.0; // время прихода заявки
            Queue<WorkStationPackage> vectorRequests = new PriorityQueue<>(new ComparatorPackageTimeAdd()); // создаём вектор заявок, сортированных по времени прихода(очередь с приоритетом с сортировкой по времени прихода)
            for (int i = 0; i < optionData.getCountWorkStation() * optionData.getSredCountPackageOnOneWorkStatinon(); i++) // цикл по всем пакетам, размер цикла кол-во раб станций * среднее кол-во пакетов
            {
                WorkStationPackage workStationPackage = new WorkStationPackage(); // создаем пакет
                double randomNumber = randomTimeAdd.nextDouble(); // генерируем время время
                // длина пуассона - рассчитываем расстояние между отрезками времени
                double timeLengthPuasson = Math.log(randomNumber) * -1 / (double)optionData.getSredCountPackageOnOneWorkStatinon();
                time += timeLengthPuasson;
                workStationPackage.setTimeAdd(time);
                workStationPackage.setCountBits(
                        optionData.getBarriedGenerationCountBits().getFirst() +
                        randomCountBits.nextInt(optionData.getBarriedGenerationCountBits().getSecond() - optionData.getBarriedGenerationCountBits().getFirst() + 1)); // генерируем колво бит
                workStationPackage.setTimeBit((
                        optionData.getBarriedGenerationTimeBit().getFirst() +
                        randomTimeBit.nextInt(optionData.getBarriedGenerationTimeBit().getSecond() -  optionData.getBarriedGenerationTimeBit().getFirst() + 1)) * 100); // генерируем время передачи 1 бита
                workStationPackage.setPriority(optionData.getGeneratorPriority().getPriority(randomPriority.nextDouble())); // генерируем приоритет
                vectorRequests.add(workStationPackage); // добавляем пакет в вектор
            }
            System.out.println("Пакеты сгенерированы и отправлены");


            // перемещаем все созданные пакеты из вектора в очередь -> время не учитывается
            while (!vectorRequests.isEmpty()) // копируем весь вектор в очередь заявок по приоритетам
            {
                queuePackagePriority.add(vectorRequests.poll());
            }
            System.out.println("Пакеты поставлены в очередь");

            // тут выполняется моделирование на каналах -> пока вся очередь не освободится
            boolean isEnd = false; // флаг остановки цикла моделирования
            while (!isEnd)  // одна итерация == 100 нс
            {
                allTime = allTime.add(BigInteger.valueOf(100)); // приращиваем общее время
                iterationTime = iterationTime.add(BigInteger.valueOf(100)); // приращиваем время текущей итерации
                if (iterationTime.compareTo(countIterationModeling) == 0) // если время итерации закончилось (0.5с)
                {
                    countCurrentSeconds += optionData.getCountLoopSeconds(); // приращиваем общее время в сек
                    if(countCurrentSeconds > timeModeling) // если время моделирования закончилось
                    {
                        break; // прерываем итерацию
                    }
                    // выводим в консоль
                    System.out.println("Текущее время: " + countCurrentSeconds + "s");
                    System.out.println("Количество пакетов в очереди: " + queuePackagePriority.size() + "");
                    System.out.println("Количество обработанных пакетов: " + countSendsPackage);
                    System.out.println("---------------------\n");
                    countCurrentSecondsGui = countCurrentSeconds;
                    countSendsPackageGui = countSendsPackage;
                    // выводим на график
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            packageQueueData.add(new XYChart.Data(countCurrentSecondsGui, queuePackagePriority.size()));
                            packageIsWorkedData.add(new XYChart.Data(countCurrentSecondsGui, countSendsPackageGui));
                            packageQueue.setData(packageQueueData);
                            packageIsWorked.setData(packageIsWorkedData);
                        }
                    });

                    // приращиваем общее кол-во пакетов
                    allPackage = allPackage.add(BigInteger.valueOf(countSendsPackage));
                    iterationTime = BigInteger.valueOf(0); // обнуляем время итерации
                    countSendsPackage = 0; // обнуляем кол-во переданных пакетов
                }
                // опрашиваем 4 канала
                // если канал занят - инкрементируем ему выполнение
                // если канал пуст - помещаем туда пакет из очереди (если он есть) и инкрементируем ему выполнение
                for (int j = 0; j < channels.size(); ++j)
                {
                    if (channels.get(j).isSend()) // если канал передан
                    {
                        countSendsPackage++; // инкрементируем кол-во переданных пакетов
                        if (!queuePackagePriority.isEmpty()) // проверяем очередь на пустоту
                        {
                            channels.set(j,queuePackagePriority.poll()); // если не пуста - выталкиваем в освободившийся канал очереддной пакет
                        }
                        else
                        {
                            isEnd = true; // иначе помечем икл как завершенный и прерываем итерацию
                            break;
                        }
                    }
                    channels.get(j).SendBit(); // если канал занят - передаём очередную порцию бит
                }
            }

        }
        System.out.println("Количество пакетов, которые были переданы: " + allPackage);
    }
}
