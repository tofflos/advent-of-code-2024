void main() throws IOException {

    var words = Files.lines(Path.of("4.txt")).map(String::toCharArray).toArray(char[][]::new);
    var count1 = 0;
    var count2 = 0;

    for (int y = 0; y < words.length; y++) {
        for (int x = 0; x < words[y].length; x++) {
            char c = words[y][x];

            if (c == 'X') {
                count1 += count1(words, x, y);
            }

            if (c == 'A') {
                count2 += count2(words, x, y);
            }
        }
    }

    System.out.println(count1);
    System.out.println(count2);
}

long count1(char[][] words, int x, int y) {
    var candidates = new ArrayList<String>();
    var directions = new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    for (var direction : directions) {

        var builder = new StringBuilder();
        var dx = direction[0];
        var dy = direction[1];
        var px = x;
        var py = y;

        for (int i = 0; i < 4; i++) {
            if (0 <= py && py < words.length && 0 <= px && px < words[py].length) {
                builder.append(words[py][px]);
            }

            px = px + dx;
            py = py + dy;
        }

        candidates.add(builder.toString());
    }

    return candidates.stream()
            .filter("XMAS"::equals)
            .count();
}

long count2(char[][] words, int x, int y) {
    var count = 0;

    try {
        if ((words[y + 1][x + 1] == 'M' && words[y - 1][x - 1] == 'S'
                || words[y + 1][x + 1] == 'S' && words[y - 1][x - 1] == 'M')
                && (words[y - 1][x + 1] == 'M' && words[y + 1][x - 1] == 'S'
                || words[y - 1][x + 1] == 'S' && words[y + 1][x - 1] == 'M')) {
            count++;
        }
    } catch (ArrayIndexOutOfBoundsException ex) {
        return 0;
    }

    return count;
}
