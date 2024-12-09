void main() throws IOException {
    var map = Files.lines(Path.of("08.txt"))
            .map(String::toCharArray)
            .toArray(char[][]::new);

    var nodes = new ArrayList<Node>();

    for (int y = 0; y < map.length; y++) {
        for (int x = 0; x < map[y].length; x++) {
            var c = map[y][x];

            if (c != '.') {
                nodes.add(new Node(x, y, c));
            }
        }
    }

    var maxY = map.length;
    var maxX = map[0].length;
    var groups = nodes.stream().collect(Collectors.groupingBy(Node::c));

    System.out.println(part1(groups, maxX, maxY));
    System.out.println(part2(groups, maxX, maxY));
}

long part1(Map<Character, List<Node>> groups, int maxX, int maxY) {
    var antinodes = new ArrayList<Node>();

    for (var group : groups.values()) {
        for (var n1 : group) {
            for (var n2 : group) {
                if (n1 != n2) {
                    var dx = n1.x - n2.x;
                    var dy = n1.y - n2.y;

                    antinodes.add(new Node(n1.x + dx, n1.y + dy, n1.c));
                }
            }
        }
    }

    return antinodes.stream()
            .filter(n -> 0 <= n.x && n.x < maxX && 0 <= n.y && n.y < maxY)
            .map(node -> new int[]{node.x, node.y})
            .map(Arrays::toString)
            .distinct()
            .count();
}

long part2(Map<Character, List<Node>> groups, int maxX, int maxY) {
    var antinodes = new ArrayList<Node>();

    for (var group : groups.values()) {
        for (var n1 : group) {
            for (var n2 : group) {
                if (n1 != n2) {
                    var dx = n1.x - n2.x;
                    var dy = n1.y - n2.y;

                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        var antinode = new Node(n1.x + i * dx, n1.y + i * dy, n1.c);

                        if (!(0 <= antinode.x && antinode.x < maxX && 0 <= antinode.y && antinode.y < maxY)) {
                            break;
                        }

                        antinodes.add(antinode);
                    }
                }
            }
        }
    }

    return antinodes.stream()
            .filter(n -> 0 <= n.x && n.x < maxX && 0 <= n.y && n.y < maxY)
            .map(node -> new int[]{node.x, node.y})
            .map(Arrays::toString)
            .distinct()
            .count();
}

record Node(int x, int y, char c) {

}

