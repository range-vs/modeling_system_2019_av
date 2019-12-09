package sample;

import java.util.ArrayList;

public class Priority {

    private ArrayList<Pair<Double, Double>> barriedPriority;
    private ArrayList<PriorityValue> prioritets;

    public Priority(double l, double m, double h)
    {
        // конструктор
        barriedPriority = new ArrayList<Pair<Double, Double>>();
        barriedPriority.add(new Pair<Double, Double>(0.0, l)); // создаем первый промежуток приоритетов
        barriedPriority.add(new Pair<Double, Double>(l, l + m)); // второй
        barriedPriority.add(new Pair<Double, Double>(l + m, 1.0)); // третий
        prioritets = new ArrayList<PriorityValue>() {{
            add(PriorityValue.PRIORITY_LOW);
            add(PriorityValue.PRIORITY_MEDIUM);
            add(PriorityValue.PRIORITY_HIGH);
        }}; // создаем массив приоритетов
    }

    public PriorityValue getPriority(double value)
    {
        // получаем приоритет по рандромному числу
        for (int i = 0; i < barriedPriority.size(); i++) // перебираем все границы приоритетов
        {
            if (value >= barriedPriority.get(i).getFirst() && value <= barriedPriority.get(i).getSecond()) // и проверяем рандомное число на попадение в эти границы
            {
                return prioritets.get(i); // в случае успеха - возврат нужного приоритета
            }
        }
        return PriorityValue.PRIORITY_ERROR; // иначе ошибка
    }

}
