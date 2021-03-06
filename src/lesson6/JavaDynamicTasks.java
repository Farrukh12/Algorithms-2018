package lesson6;

import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.min;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Средняя
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    // Трудоемкость: T = O(n * n)
    // Ресурсоемкость: R = O(n)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int n = list.size();
        if (n == 0 || n == 1) return list;
        int[] prev = new int[n];
        int[] d = new int[n];

        Arrays.fill(d, 1);
        Arrays.fill(prev, -1);

        int pos = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (list.get(j) < list.get(i) && d[j] + 1 > d[i]) {
                    d[i] = d[j] + 1;
                    prev[i] = j;
                    if (d[pos] < d[i]) {
                        pos = i;
                    }
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        int index = pos;
        while (index >= 0) {
            result.add(list.get(index));
            index = prev[index];
        }
        List<Integer> answer = new ArrayList<>();
        for (int i = result.size() - 1; i >= 0; i--) {
            answer.add(result.get(i));
        }
        return answer;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Сложная
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    //* Трудоемкость: T = O(height * width);
    //* Ресурсоемкость: R = O(n)
    public static int shortestPathOnField(String inputName) throws IOException {
        Scanner cin = new Scanner(new File(inputName));
        int height = 0;
        int width = 0;
        List<String[]> list = new ArrayList<>();

        while (cin.hasNextLine()) {
            String line = cin.nextLine();
            if (line.matches("^[0-9]( [0-9])*$")) {
                String[] numbers = line.split(" ");
                list.add(numbers);
                width = numbers.length;
                height++;
            }
        }
        cin.close();

        String[][] field = new String[height][width];
        int[][] matrix = new int[height][width];

        for (int i = 0; i < height; i++) {
            field[i] = list.get(i);
        }

        matrix[0][0] = Integer.parseInt(field[0][0]);

        for (int i = 1; i < height; i++) {
            matrix[i][0] = matrix[i - 1][0] + Integer.parseInt(field[i][0]);
        }

        for (int i = 1; i < width; i++) {
            matrix[0][i] = matrix[0][i - 1] + Integer.parseInt(field[0][i]);
        }

        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                matrix[i][j] = Math.min(matrix[i - 1][j - 1], Math.min(matrix[i][j - 1], matrix[i - 1][j])) + Integer.parseInt(field[i][j]);
            }
        }

        return matrix[height - 1][width - 1];
    }


    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
