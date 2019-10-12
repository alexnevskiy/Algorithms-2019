package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Map<Address, List<String>> map = new HashMap<>();
        String line;
        try (BufferedReader buffer = new BufferedReader(new FileReader(new File(inputName)))){
            while ((line = buffer.readLine()) != null) {  //  T=O(line), line - количество строк в файле
                List<String> list = new ArrayList<>();
                if (line.matches("([A-ZА-ЯЁa-zа-яё]+\\s*)+-\\s+([A-ZА-ЯЁa-zа-яё-]+\\s*)+\\d+"))
                {
                    String trim = line.replaceAll("\\s+", " ").trim();
                    String[] split = trim.split(" - ");
                    String person = split[0].trim();
                    Address address = new Address(split[1].trim());
                    if (map.get(address) == null) {
                        list.add(person);
                        map.put(address, list);
                    } else {
                        list = map.get(address);
                        list.add(person);
                    }
                } else throw new IllegalArgumentException("Некорректный ввод.");
            }
        }

        List<Address> addressList = new ArrayList<>(map.keySet());
        Sorts.qckSort(addressList);  //  T=O(n*log(n))  Быстрая сортировка (Хоара)

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)))){
            for (Address anAddressList : addressList) {  //  T=O(address) address - количество адресов в листе
                StringBuilder str = new StringBuilder(anAddressList.toString() + " - ");
                List<String> nameList = new ArrayList<>(map.get(anAddressList));
                Sorts.qckSort(nameList);  //  T=O(n*log(n))  Быстрая сортировка (Хоара)
                for (int j = 0; j < nameList.size(); j++) {  //  T=O(name) name - количество имён в листе
                    if (j != nameList.size() - 1) str.append(nameList.get(j)).append(", ");
                    else str.append(nameList.get(j));
                }
                writer.write(str.toString());
                writer.newLine();
            }
        }
    }  //  Вывод: T=O(n*log(n)), R=O(n)

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        String line;
        int[] tempArray = new int[7731];
        try (BufferedReader buffer = new BufferedReader(new FileReader(new File(inputName)))){
            while ((line = buffer.readLine()) != null) tempArray[(int)(Double.parseDouble(line) * 10) + 2730]++;
        }  //  T=O(line), line - количество строк в файле
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)))){
            for (int i = 0; i < tempArray.length; i++) {   //  T=O(n)
                while (tempArray[i] != 0) {
                    writer.write(String.valueOf((double)(i - 2730) / 10));
                    writer.newLine();
                    tempArray[i]--;
                }
            }
        }
    }  //  Вывод: T=O(n), R=O(1)

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}

/**
 * Вспомогательный класс для решения задачи sortAddresses
 */

class Address implements Comparable<Address> {

    private String address;
    private int number;

    Address(String input) {
        address = input.split(" ")[0];
        number = Integer.parseInt(input.split(" ")[1]);
    }

    @Override
    public int compareTo(Address other) {
        if (address.compareTo(other.address) > 0) return 1;
        else if (address.compareTo(other.address) == 0) {
            return Integer.compare(number, other.number);
        } else return -1;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof Address) {
            Address newOther = (Address) other;
            return address.equals(newOther.address) && number == newOther.number;
        }
        return false;
    }

    @Override
    public String toString() {
        return address + " " + number;
    }

    @Override
    public int hashCode() {
        int hash = address.hashCode();
        hash = 55 * hash + number;
        return hash;
    }
}