void main() throws IOException {
    var map = Files.lines(Path.of("10.txt"))
            .map(line -> line.split(""))
            .map(arr -> Arrays.stream(arr).mapToInt(Integer::parseInt).toArray())
            .toArray(int[][]::new);

    var sum = 0L;
    var trails = new ArrayDeque<List<Point>>();

    for (int y = 0; y < map.length; y++) {
        for (int x = 0; x < map[y].length; x++) {
            if (map[y][x] == 0) {
                var t = walk(map, x, y);
                sum += t.stream().map(List::getLast).distinct().count();
                trails.addAll(t);
            }
        }
    }

    System.out.println(sum);
    System.out.println(trails.size());
}

Deque<List<Point>> walk(int[][] map, int x, int y) {

    var directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    var branches = new ArrayDeque<List<Point>>();
    var results = new ArrayDeque<List<Point>>();

    branches.add(new ArrayList<>(List.of(new Point(x, y))));

    while (!branches.isEmpty()) {
        var current = branches.pop();

        var p = current.getLast();

        if (map[p.y][p.x] == 9) {
            results.add(current);
            continue;
        }

        for (var direction : directions) {
            var n = new Point(p.x + direction[0], p.y + direction[1]);

            if (0 <= n.y && n.y < map.length && 0 <= n.x && n.x < map[n.y].length
                    && map[p.y][p.x] == map[n.y][n.x] - 1) {
                var next = new ArrayList<>(current);
                next.add(n);
                branches.push(next);
            }
        }
    }

    return results;
}

record Point(int x, int y) {

}