void main() throws IOException {
    var s = Files.readString(Path.of("14.txt"));
    var pattern = Pattern.compile("-?\\d+");

    var robots = pattern.matcher(s).results()
            .map(MatchResult::group)
            .map(Integer::parseInt)
            .gather(Gatherers.windowFixed(4))
            .map(w -> new Robot(w.get(0), w.get(1), w.get(2), w.get(3)))
            .toList();

    var maxX = 101;
    var maxY = 103;

    List<Robot> current = new ArrayList<>(robots);

    for (int i = 0; i < 100; i++) {
        current = current.stream()
                .map(robot -> new Robot(Math.floorMod(robot.x + robot.dx, maxX), Math.floorMod(robot.y + robot.dy, maxY), robot.dx, robot.dy))
                .toList();
    }

    var quadrants = current.stream().collect(Collectors.groupingBy(r -> {
        if (r.x == maxX / 2 || r.y == maxY / 2) {
            return "NONE";
        }
        if (0 <= r.x && r.x < maxX / 2 && 0 <= r.y && r.y < maxY / 2) {
            return "Q1";
        }
        if (maxX / 2 <= r.x && r.x < maxX && 0 <= r.y && r.y < maxY / 2) {
            return "Q2";
        }
        if (0 <= r.x && r.x < maxX / 2 && maxY / 2 <= r.y && r.y < maxY) {
            return "Q3";
        }
        if (maxX / 2 <= r.x && r.x < maxX && maxY / 2 <= r.y && r.y < maxY) {
            return "Q4";
        }

        throw new IllegalArgumentException();
    }));

    var safety = quadrants.entrySet().stream()
            .filter(entry -> !"NONE".equals(entry.getKey()))
            .mapToInt(entry -> entry.getValue().size())
            .reduce(1, Math::multiplyExact);

    System.out.println(safety);

    current = new ArrayList<>(robots);

    for (int i = 0; i < 10000; i++) {
        System.out.println(i);
        draw(current, maxX, maxY);

        current = current.stream()
                .map(robot -> new Robot(Math.floorMod(robot.x + robot.dx, maxX), Math.floorMod(robot.y + robot.dy, maxY), robot.dx, robot.dy))
                .toList();
    }

    System.out.println(7687); // Scroll down to iteration 7687
}

void draw(List<Robot> robots, int maxX, int maxY) {

    var lines = IntStream.range(0, maxY)
            .mapToObj(_ -> new StringBuilder(".".repeat(maxX)))
            .toList();

    robots.forEach(r -> lines.get(r.y).setCharAt(r.x, '█'));

    System.out.println(lines.stream().collect(Collectors.joining(System.lineSeparator())));
}

record Robot(int x, int y, int dx, int dy) {

}