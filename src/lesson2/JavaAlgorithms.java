package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    static public String longestCommonSubstring(String first, String second) {
        int[][] matrix = new int[first.length()][second.length()];
        int coincidencesNumber = 0;
        int lastCharIndex = 0;
        for (int i = 0; i < first.length(); i++) {  //  T=O(a)
            for (int j = 0; j < second.length(); j++) {  //  T=O(b)
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) matrix[i][j] = 1;
                    else matrix[i][j] = matrix[i - 1][j - 1] + 1;
                }
                if (matrix[i][j] > coincidencesNumber) {
                    coincidencesNumber = matrix[i][j];
                    lastCharIndex = i + 1;
                }
            }
        }
        if (coincidencesNumber == 0) return "";
        else return first.substring(lastCharIndex - coincidencesNumber, lastCharIndex);
    }  //  Вывод: T=O(a*b), R=O(a*b)

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        int answer = 0;
        boolean[] array = new boolean[limit + 1];
        if (limit <= 1) return 0;
        for (int i = 2; i <= limit; i++) array[i] = true;
        for (int i = 2; i <= limit; i++) {
            if (array[i]) {
                for (int j = i; j <= limit; j += i) {
                    array[j] = false;
                }
                answer++;
            }
        }
        return answer;
    }  //  Вывод: T=O(n(log(log(n))), R=O(n)

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws IOException {
        Set<String> set = new HashSet<>();
        String line;
        List<String[]> list = new ArrayList<>();

        try (BufferedReader buffer = new BufferedReader(new FileReader(new File(inputName)))) {
            while ((line = buffer.readLine()) != null) list.add(line.split("\\s+"));
        }

        char[][] matrix = new char[list.size()][list.get(0).length];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).length; j++) {
                matrix[i][j] = list.get(i)[j].charAt(0);
            }
        }

        return set;
    }
}

class Board {

    private int row;
    private int column;

    Board(int rowId, int colId) {
        row = rowId;
        column = colId;
    }

    public boolean inRange(int rowId, int colId, int rowSize, int colSize) {
        return 0 <= rowId && rowId < rowSize && 0 <= colId && colId < colSize;
    }

    public Board[] getNeighbours(int rowId, int colId) {
        Board[] surrounding = new Board[4];
        surrounding[0] = new Board(-1 + rowId, colId);
        surrounding[1] = new Board(rowId, 1 + colId);
        surrounding[2] = new Board(1 + rowId, colId);
        surrounding[3] = new Board(rowId, -1 + colId);
        return surrounding;
    }
}