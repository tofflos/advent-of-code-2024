void main() throws IOException {
    var bytes = Files.lines(Path.of("18.txt"))
            .map(line -> line.split(","))
            .map(arr -> new Byte(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])))
            .toList();

    var size = 71;

    var map = IntStream.range(0, size).mapToObj(_ -> ".".repeat(size).toCharArray()).toArray(char[][]::new);
    bytes.stream().limit(1024).forEach(b -> map[b.y][b.x] = '#');
    System.out.println(walk(map));

    var n = 0;

    for (int i = 1024; i < bytes.size(); i++) {
        if (walk(map) == Integer.MAX_VALUE) {
            break;
        }

        map[bytes.get(i).y][bytes.get(i).x] = '#';
        n = i;
    }

    System.out.println(bytes.get(n));
}

int walk(char[][] map) {
    var costs = Arrays.stream(map)
            .mapToInt(row -> row.length)
            .mapToObj(length -> {
                var row = new int[length];
                Arrays.fill(row, Integer.MAX_VALUE);
                return row;
            })
            .toArray(int[][]::new);
    var directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    var current = new HashSet<Byte>();
    current.add(new Byte(0, 0));
    costs[0][0] = 0;

    while (true) {
        var next = new HashSet<Byte>();

        for (var c : current) {
            for (var d : directions) {
                var n = new Byte(c.x + d[0], c.y + d[1]);

                if (0 <= n.y && n.y < map.length && 0 <= n.x && n.x < map[n.y].length
                        && map[n.y][n.x] != '#'
                        && costs[c.y][c.x] + 1 < costs[n.y][n.x]) {
                    costs[n.y][n.x] = costs[c.y][c.x] + 1;
                    next.add(n);
                }
            }
        }

        if (current.equals(next)) {
            break;
        }

        current = next;
    }

    return costs[costs.length - 1][costs[costs.length - 1].length - 1];
}

record Byte(int x, int y) {


}