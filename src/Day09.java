void main() throws IOException {
    var map = Files.readString(Path.of("09.txt"));
    var blocks = new ArrayList<Integer>();

    for (int i = 0; i < map.length(); i++) {
        var size = Character.getNumericValue(map.charAt(i));
        var t = Collections.nCopies(size, i % 2 == 0 ? i / 2 : -1);
        blocks.addAll(t);
    }

    System.out.println(checksum(compact1(new ArrayList<>(blocks))));
    System.out.println(checksum(compact2(new ArrayList<>(blocks))));
}

List<Integer> compact1(List<Integer> blocks) {
    for (int i1 = 0; i1 < blocks.size(); i1++) {
        var n1 = blocks.get(i1);

        if (n1 == -1) {
            var i2 = lastIndexOfDigit(blocks);

            if (i2 < i1) {
                break;
            }

            var n2 = blocks.get(i2);
            blocks.set(i1, n2);
            blocks.set(i2, n1);
        }
    }

    return blocks;
}

List<Integer> compact2(List<Integer> blocks) {
    var sizes = blocks.stream()
            .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

    var files = sizes.keySet().stream()
            .filter(key -> key >= 0)
            .sorted((a, b) -> b - a)
            .toList();

    for (var file : files) {
        var size = sizes.get(file).intValue();
        var gapIndex = Collections.indexOfSubList(blocks, Collections.nCopies(size, -1));

        if (gapIndex == -1) {
            continue;
        }

        var fileIndex = blocks.indexOf(file);

        if (gapIndex > fileIndex) {
            continue;
        }

        for (int i = 0; i < size; i++) {
            blocks.set(gapIndex + i, file);
            blocks.set(fileIndex + i, -1);
        }
    }

    return blocks;
}

int lastIndexOfDigit(List<Integer> digits) {
    for (int i = 0; i < digits.size(); i++) {
        if (digits.get(digits.size() - i - 1) != -1) {
            return digits.size() - i - 1;
        }
    }

    return -1;
}

long checksum(List<Integer> blocks) {
    var checksum = 0L;

    for (int i = 0; i < blocks.size(); i++) {
        var n = blocks.get(i);

        if (n == -1) {
            continue;
        }

        checksum += (long) i * n;
    }

    return checksum;
}
