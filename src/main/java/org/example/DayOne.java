package org.example;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DayOne {

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println(new DayOne().solutionToPartOne());
        System.out.println(new DayOne().solutionToPartTwo());
    }

    record Tuple<T>(T left, T right) {
    }

    long solutionToPartOne() throws URISyntaxException, IOException {
        var lists = parseInputFile("inputs/01/input");

        var leftValues = lists.left;
        var rightValues = lists.right;

        Collections.sort(leftValues);
        Collections.sort(rightValues);

        var totalDistance = 0L;
        for (int i = 0; i < leftValues.size(); i++) {
            var difference = Math.abs(leftValues.get(i) - rightValues.get(i));
            totalDistance += difference;
        }
        return totalDistance;


    }

    long solutionToPartTwo() throws URISyntaxException, IOException {
        var lists = parseInputFile("inputs/01/input");
        var leftValues = lists.left;
        var rightValues = lists.right;

        var rightOccurrences = rightValues.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Function<Long, Long> similarityScore = leftValue -> leftValue * rightOccurrences.getOrDefault(leftValue, 0L);
        return leftValues.stream().distinct()
                .map(similarityScore)
                .reduce(Long::sum)
                .orElse(0L);
    }

    Tuple<List<Long>> parseInputFile(String resourceName) throws IOException, URISyntaxException {
        var path = Path.of(DayOne.class.getClassLoader().getResource(resourceName).toURI());
        try (var lines = Files.lines(path)) {
            return lines.map(line -> line.split(" {3}"))
                    .collect(Collectors.teeing(
                            Collectors.mapping(split -> Long.parseLong(split[0]), Collectors.toList()),
                            Collectors.mapping(split -> Long.parseLong(split[1]), Collectors.toList()),
                            Tuple::new
                    ));
        }
    }
}