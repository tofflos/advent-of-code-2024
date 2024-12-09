void main() throws IOException {

    var memory = Files.readString(Path.of("03.txt"));

    var part1 = Pattern.compile("mul\\((\\d+),(\\d+)\\)")
            .matcher(memory)
            .results()
            .mapToInt(r -> Integer.parseInt(r.group(1)) * Integer.parseInt(r.group(2)))
            .sum();

    System.out.println(part1);

    var results = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
            .matcher(memory)
            .results()
            .toList();

    var enabled = true;
    var part2 = 0;

    for (var result : results) {
        switch (result.group()) {
            case "do()" -> enabled = true;
            case "don't()" -> enabled = false;
            default -> {
                if (enabled) {
                    part2 += Integer.parseInt(result.group(1)) * Integer.parseInt(result.group(2));
                }
            }
        }
    }

    System.out.println(part2);
}
