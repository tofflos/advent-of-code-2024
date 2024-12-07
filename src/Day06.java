void main() throws IOException {
    var lab = Files.lines(Path.of("6.txt")).map(String::toCharArray).toArray(char[][]::new);
    int startx = -1;
    int starty = -1;

    outer:
    for (int y = 0; y < lab.length; y++) {
        for (int x = 0; x < lab[y].length; x++) {
            char c = lab[y][x];

            if (c == '^') {
                startx = x;
                starty = y;
                break outer;
            }
        }
    }

    var modifiedLabs = new ArrayList<char[][]>();

    for (int y = 0; y < lab.length; y++) {
        for (int x = 0; x < lab[y].length; x++) {
            char c = lab[y][x];

            if (c == '.') {
                var modifiedLab = Arrays.stream(lab).map(arr -> Arrays.copyOf(arr, arr.length)).toArray(char[][]::new);
                modifiedLab[y][x] = '#';
                modifiedLabs.add(modifiedLab);
            }
        }
    }

    System.out.println(walk(lab, startx, starty));

    var count = 0;

    for (var modifiedLab : modifiedLabs) {
        if (isLoop(modifiedLab, startx, starty)) {
            count++;
        }
    }

    System.out.println(count);
}

static int walk(char[][] lab, int x, int y) {

    var directions = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    var rotation = 0;
    var px = x;
    var py = y;

    try {
        while (0 < py && py < lab.length && 0 < px && px < lab[py].length) {
            lab[py][px] = 'X';

            if (lab[py + directions[rotation][1]][px + directions[rotation][0]] != '#') {
                px += directions[rotation][0];
                py += directions[rotation][1];
            } else {
                rotation = (rotation + 1) % 4;
            }
        }

    } catch (ArrayIndexOutOfBoundsException _) {

    }

    var sum = 0;

    for (int j = 0; j < lab.length; j++) {
        for (int i = 0; i < lab[j].length; i++) {
            char c = lab[j][i];

            if (c == 'X') {
                sum++;
            }
        }
    }

    return sum;
}


static boolean isLoop(char[][] lab, int x, int y) {

    record Visit(int x, int y, int r) {
    }

    var visited = new HashSet<Visit>();

    var directions = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    var rotation = 0;
    var px = x;
    var py = y;

    try {
        while (0 < py && py < lab.length && 0 < px && px < lab[py].length) {
            lab[py][px] = 'X';

            var visit = new Visit(px, py, rotation);

            if (visited.contains(visit)) {
                return true;
            }

            visited.add(visit);

            if (lab[py + directions[rotation][1]][px + directions[rotation][0]] != '#') {
                px += directions[rotation][0];
                py += directions[rotation][1];
            } else {
                rotation = (rotation + 1) % 4;
            }
        }

    } catch (ArrayIndexOutOfBoundsException _) {

    }

    return false;
}