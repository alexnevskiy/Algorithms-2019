package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.util.*;
import java.util.List;

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

        try (BufferedReader buffer = new BufferedReader(new FileReader(new File(inputName)))) {  //  Парсим строку и
            while ((line = buffer.readLine()) != null) list.add(line.split("\\s+"));  //  добавляем в лист
        }

        int numberOfRows = list.size();
        int numberOfColumns = list.get(0).length;
        char[][] matrix = new char[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {  //  Заполняем матрицу чарами (буквами) из листа
            for (int j = 0; j < numberOfColumns; j++) {  // T=O(i*j)
                matrix[i][j] = list.get(i)[j].charAt(0);
            }
        }

        Cell cell;
        List<Cell> cellList = new ArrayList<>();
        int numberOfLetters = 1;
        for (String word: words) {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {  //  T=O(i*j*w), где w - words (количество возможных слов)
                    if (matrix[i][j] == word.charAt(0)) {  //  Делаем проверку на совпадение первой буквы слова
                        cell = new Cell(i, j);
                        if (cell.wordSearch(word, matrix, numberOfLetters, numberOfRows, numberOfColumns, cellList)) set.add(word);
                        numberOfLetters = 1;  //  Сбрасываем количество знаков и лист до начальных значений после
                        cellList.clear();  //  проверки каждой клетки
                    }
                }
            }
        }
        return set;
    }
}  //  Вывод: T=O(n*m*k*3^(n*m)), R=O(n*m), где n - ширина матрицы, m - высота, k - количество искомых слов

/**
 * Вспомогательный класс Cell для решения задачи про балду. Реализует клетку матрицы.
 * В конструкторе используются координаты буквы, то есть индексы ряда и столбца в двумерном массиве.
 *
 * Реализованы методы:
 *
 * inRange(int rowSize, int colSize) - проверка на выход за границы матрицы
 *
 * getNeighbours(int rowSize, int colSize) - выводит массив из клеток, в которых может стоять следующая буква слова,
 * то есть предполагаемые координаты
 *
 * wordSearch(String word, char[][] matrix, int numberOfLetters, int width, int high, List<Cell> list) - самый важный
 * метод в данном классе, который реализует поиск слова по чарам в матрице
 */

class Cell {

    private int row;
    private int column;

    Cell(int rowId, int colId) {
        row = rowId;
        column = colId;
    }

    public boolean inRange(int rowSize, int colSize) {
        return 0 <= row && row < rowSize && 0 <= column && column < colSize;
    }

    public Cell[] getNeighbours() {  //  Создаём массив из клеток, которые расположены
        Cell[] surrounding = new Cell[4];  //  в таком порядке относительно буквы в матрице (по часовой стрелке):
        surrounding[0] = new Cell(-1 + row, column);  //  Сверху
        surrounding[1] = new Cell(row, 1 + column);  //  Справа
        surrounding[2] = new Cell(1 + row, column);  //  Снизу
        surrounding[3] = new Cell(row, -1 + column);  //  Слева
        return surrounding;
    }

    public boolean wordSearch(String word, char[][] matrix, int numberOfLetters, int width, int high, List<Cell> list) {
        if (numberOfLetters == word.length()) return true;  //  Проверка на длину слова и количество букв, которые
        numberOfLetters++;  //  уже обработаны
        Cell[] surrounding = this.getNeighbours();
        for (Cell side: surrounding) {  //  Проходимся по соседним клеткам от буквы, на которой был вызван метод
            if (side.inRange(width, high) && matrix[side.row][side.column] == word.charAt(numberOfLetters - 1)
                    && !list.contains(side)) {  //  Если клетка не за пределами матрицы, буква в ней равна следующей
                list.add(this);  // букве в слове и позиция клетки ещё не была использована в данной проверке, то
                if (side.wordSearch(word, matrix, numberOfLetters, width, high, list)) return true;  //  добавляем её
            }  //  в лист и запускаем этот же метод, но уже на следующую букву слова
        }
        return false;  //  T=O(3^(n*m))  В начале рекурсии у нас есть все 4 стороны, далее при переходе на следующую
    }  //  букву прошлая будет одной из 4 сторон, куда мы можем пойти, соответственно, в неё мы никогда не попадём,
  //  отсюда получается, что остаётся всегда лишь 3 стороны для проверки на совпадение
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof Cell) {
            Cell newOther = (Cell) other;
            return row == (newOther.row) && column == newOther.column;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}