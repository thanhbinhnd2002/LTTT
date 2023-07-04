import java.util.*;




class HuffmanShannonFano {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập vào chuỗi ký tự không dấu: ");
        String input = scanner.nextLine();
        scanner.close();

        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        List<Node> nodeList = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            nodeList.add(new Node(entry.getKey(), entry.getValue()));
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new HuffmanComparator());
        priorityQueue.addAll(nodeList);

        while (priorityQueue.size() > 1) {
            Node leftChild = priorityQueue.poll();
            Node rightChild = priorityQueue.poll();

            Node parent = new Node('\0', leftChild.frequency + rightChild.frequency);
            parent.left = leftChild;
            parent.right = rightChild;

            priorityQueue.offer(parent);
        }

        Node root = priorityQueue.peek();
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodes(root, "", huffmanCodes);

        System.out.println("Mã huffman:");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        double huffmanEfficiency = calculateEfficiency(input.length() * 8, huffmanCodes);
        System.out.println("Hiệu suất mã hóa Huffman: " + huffmanEfficiency);

        double shannonFanoEfficiency = calculateEfficiency(input.length() * 8, shannonFanoCodes(frequencyMap));
        System.out.println("Hiệu suất mã hóa Shannon-Fano: " + shannonFanoEfficiency);
    }

    private static void generateHuffmanCodes(Node node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.character, code);
        }

        generateHuffmanCodes(node.left, code + "0", huffmanCodes);
        generateHuffmanCodes(node.right, code + "1", huffmanCodes);
    }

    private static double calculateEfficiency(int originalSize, Map<Character, String> codes) {
        int totalBits = 0;
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            totalBits += entry.getValue().length();
        }

        double efficiency = (double) totalBits / originalSize;
        double redundancy = 1 - efficiency;
        return efficiency * 100;
    }

    private static Map<Character, String> shannonFanoCodes(Map<Character, Integer> frequencyMap) {
        List<Character> characters = new ArrayList<>(frequencyMap.keySet());
        Collections.sort(characters, (c1, c2) -> frequencyMap.get(c2) - frequencyMap.get(c1));

        return generateShannonFanoCodes(characters, 0, characters.size() - 1);
    }

    private static Map<Character, String> generateShannonFanoCodes(List<Character> characters, int start, int end) {
        Map<Character, String> codes = new HashMap<>();

        if (start == end) {
            codes.put(characters.get(start), "0");
            return codes;
        }

        int totalFrequency = 0;
        for (int i = start; i <= end; i++) {
            totalFrequency += frequencyMap.get(characters.get(i));
        }

        int cumulativeFrequency = 0;
        int mid = start;
        int previousMid = mid;
        int frequencyDiff1 = totalFrequency;
        int frequencyDiff2;

        while (frequencyDiff1 >= frequencyDiff2 && mid < end) {
            previousMid = mid;
            cumulativeFrequency += frequencyMap.get(characters.get(mid));
            frequencyDiff1 = Math.abs(totalFrequency - 2 * cumulativeFrequency);
            frequencyDiff2 = Math.abs(totalFrequency - 2 * (cumulativeFrequency + frequencyMap.get(characters.get(mid + 1))));
            mid++;
        }

        for (int i = start; i <= previousMid; i++) {
            codes.put(characters.get(i), "0");
        }

        for (int i = mid; i <= end; i++) {
            codes.put(characters.get(i), "1");
        }

        codes.putAll(generateShannonFanoCodes(characters, start, previousMid));
        codes.putAll(generateShannonFanoCodes(characters, mid, end));

        return codes;
    }
}
