void main() throws IOException {
    var garden = Files.lines(Path.of("12.txt")).map(String::toCharArray).toArray(char[][]::new);
    var regions = new HashSet<Set<Plot>>();

    for (int y = 0; y < garden.length; y++) {
        for (int x = 0; x < garden[y].length; x++) {
            regions.add(region(garden, x, y));
        }
    }

    System.out.println(regions.stream().mapToInt(region -> price(garden, region)).sum());
    System.out.println(regions.stream().mapToInt(region -> bulkprice(garden, region)).sum());

}

int price(char[][] garden, Set<Plot> region) {
    var fences = region.stream().flatMap(plot -> fences(plot).stream()).collect(Collectors.toSet());
    fences = filterfences(fences);

    return fences.size() * region.size();
}

int bulkprice(char[][] garden, Set<Plot> region) {
    var fences = region.stream().flatMap(plot -> fences(plot).stream()).collect(Collectors.toSet());
    fences = filterfences(fences);

    var sides = 0;

    for (var f1 : fences) {
        for (var f2 : fences) {
            if (f1.x2 == f2.x1 && f1.y2 == f2.y1) {
                var dx1 = Math.abs(f1.x1 - f1.x2);
                var dy1 = Math.abs(f1.y1 - f1.y2);
                var dx2 = Math.abs(f2.x1 - f2.x2);
                var dy2 = Math.abs(f2.y1 - f2.y2);

                if (dx1 != dx2 || dy1 != dy2) {
                    sides++;
                    break;
                }
            }
        }
    }

    return sides * region.size();
}

Set<Fence> fences(Plot plot) {
    var fences = new HashSet<Fence>();
    var directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    var px = plot.x;
    var py = plot.y;

    for (var direction : directions) {
        var nx = px + direction[0];
        var ny = py + direction[1];

        fences.add(new Fence(px, py, nx, ny));

        px = nx;
        py = ny;
    }

    return fences;
}

Set<Fence> filterfences(Set<Fence> fences) {
    var result = new HashSet<Fence>();

    for (var fence : fences) {
        if (fences.stream().noneMatch(f -> fence.x1 == f.x2 && fence.y1 == f.y2 && fence.x2 == f.x1 && fence.y2 == f.y1)) {
            result.add(fence);
        }
    }

    return result;
}


Set<Plot> region(char[][] garden, int x, int y) {
    var directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    var current = new HashSet<>(List.of(new Plot(x, y)));

    while (true) {
        var next = new HashSet<>(current);

        for (var plot : current) {
            for (var direction : directions) {
                var nx = plot.x + direction[0];
                var ny = plot.y + direction[1];

                if (0 <= ny && ny < garden.length && 0 <= nx && nx < garden[ny].length
                        && garden[plot.y][plot.x] == garden[ny][nx]) {
                    next.add(new Plot(nx, ny));
                }
            }
        }

        if (next.equals(current)) {
            break;
        }

        current = next;
    }

    return current;
}

record Plot(int x, int y) {

}

record Fence(int x1, int y1, int x2, int y2) {

}