void main() throws IOException {
    var s = Files.readString(Path.of("15.txt"));
    var arr = s.split(System.lineSeparator().repeat(2));
    var movements = arr[1].lines().collect(Collectors.joining());

    var map1 = arr[0].lines().map(StringBuilder::new).toList();
    move1(map1, movements);
    System.out.println(gps(map1, 'O'));

    var map2 = arr[0].lines()
            .map(line -> line
                    .replaceAll("#", "##")
                    .replaceAll("O", "\\[\\]")
                    .replaceAll("\\.", "\\.\\.")
                    .replaceAll("@", "@\\."))
            .map(StringBuilder::new)
            .toList();
    move2(map2, movements);
    System.out.println(gps(map2, '['));
}

int gps(List<StringBuilder> map, char c) {
    var sum = 0;

    for (int y = 0; y < map.size(); y++) {
        for (int x = 0; x < map.get(y).length(); x++) {
            if (map.get(y).charAt(x) == c) {
                sum += 100 * y + x;
            }
        }
    }

    return sum;
}

void move2(List<StringBuilder> map, String movements) {
    var directions = Map.of('^', List.of(0, -1), '>', List.of(1, 0), 'v', List.of(0, 1), '<', List.of(-1, 0));
    var cx = -1;
    var cy = -1;

    for (int y = 0; y < map.size(); y++) {
        for (int x = 0; x < map.get(y).length(); x++) {
            var c = map.get(y).charAt(x);

            if (c == '@') {
                cx = x;
                cy = y;
                break;
            }
        }
    }

    for (int i = 0; i < movements.length(); i++) {
        var d = movements.charAt(i);
        var dx = directions.get(d).get(0);
        var dy = directions.get(d).get(1);
        var nx = cx + dx;
        var ny = cy + dy;
        var n = map.get(ny).charAt(nx);

        if (n == '#') {
            continue;
        }

        if (n == '.') {
            map.get(cy).setCharAt(cx, '.');
            map.get(ny).setCharAt(nx, '@');
            cx = nx;
            cy = ny;
            continue;
        }

        if ((n == '[' || n == ']')) {
            var boxes = boxes(map, nx, ny, dx, dy);

            if (boxes.stream().anyMatch(point -> map.get(point.y + dy).charAt(point.x + dx) == '#')) {
                continue;
            }

            if (dx == 0) {
                boxes.stream().sorted((a, b) -> dy == 1 ? b.y - a.y : a.y - b.y)
                        .forEach(box -> {
                            map.get(box.y + dy).setCharAt(box.x, map.get(box.y).charAt(box.x));
                            map.get(box.y).setCharAt(box.x, '.');
                        });
            }

            if (dy == 0) {
                boxes.stream().sorted((a, b) -> dx == 1 ? b.x - a.x : a.x - b.x)
                        .forEach(box -> {
                            map.get(box.y).setCharAt(box.x + dx, map.get(box.y).charAt(box.x));
                        });
            }

            map.get(ny).setCharAt(nx, '@');
            map.get(cy).setCharAt(cx, '.');
            cx = nx;
            cy = ny;
        }
    }
}

Set<Point> boxes(List<StringBuilder> map, int px, int py, int dx, int dy) {
    var current = new HashSet<>(Set.of(new Point(px, py)));

    while (true) {
        var next = new HashSet<Point>();

        for (var point : current) {
            next.add(point);

            var c = map.get(point.y).charAt(point.x);

            if (c == '[') {
                next.add(new Point(point.x + 1, point.y));
            }

            if (c == ']') {
                next.add(new Point(point.x - 1, point.y));
            }

            var nx = point.x + dx;
            var ny = point.y + dy;
            var n = map.get(ny).charAt(nx);

            if (n == '[' || n == ']') {
                next.add(new Point(nx, ny));
            }
        }

        if (current.equals(next)) {
            break;
        }

        current = next;
    }

    return current;
}

void move1(List<StringBuilder> map, String movements) {
    var directions = Map.of('^', List.of(0, -1), '>', List.of(1, 0), 'v', List.of(0, 1), '<', List.of(-1, 0));
    var cx = -1;
    var cy = -1;

    for (int y = 0; y < map.size(); y++) {
        for (int x = 0; x < map.get(y).length(); x++) {
            var c = map.get(y).charAt(x);

            if (c == '@') {
                cx = x;
                cy = y;
                break;
            }
        }
    }

    for (int i = 0; i < movements.length(); i++) {
        var d = movements.charAt(i);
        var dx = directions.get(d).get(0);
        var dy = directions.get(d).get(1);
        var nx = cx + dx;
        var ny = cy + dy;
        var n = map.get(ny).charAt(nx);

        if (n == '#') {
            continue;
        }

        if (n == '.') {
            map.get(cy).setCharAt(cx, '.');
            map.get(ny).setCharAt(nx, '@');
            cx = nx;
            cy = ny;
            continue;
        }

        if (n == 'O') {
            var tx = nx;
            var ty = ny;
            var t = map.get(ty).charAt(tx);

            while (t == 'O') {
                tx = tx + dx;
                ty = ty + dy;
                t = map.get(ty).charAt(tx);
            }

            if (t == '#') {
                continue;
            }

            map.get(cy).setCharAt(cx, '.');
            map.get(ny).setCharAt(nx, '@');
            map.get(ty).setCharAt(tx, 'O');
            cx = nx;
            cy = ny;
        }
    }
}

record Point(int x, int y) {

}