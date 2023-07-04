import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nhập cỡ ma trận xác suất
        System.out.print("Nhập số hàng (M): ");
        int M = scanner.nextInt();
        System.out.print("Nhập số cột (N): ");
        int N = scanner.nextInt();

        // Khởi tạo ma trận xác suất
        double[][] probabilities = new double[M][N];

        // Nhập giá trị xác suất và kiểm tra tính hợp lệ
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                boolean isValidProbability = false;
                while (!isValidProbability) {
                    System.out.print("Nhập xác suất P(" + i + "," + j + "): ");
                    double probability = scanner.nextDouble();
                    if (probability >= 0 && probability <= 1) {
                        probabilities[i][j] = probability;
                        isValidProbability = true;
                    } else {
                        System.out.println("Xác suất không hợp lệ. Vui lòng nhập lại.");
                    }
                }
            }
        }

        // Tính và hiển thị entropy H(X), H(Y), H(X | Y), H(Y | X), H(X, Y), H(Y) − H(Y | X), I(X; Y)
        double entropyX = calculateEntropy(getMarginalProbabilitiesX(probabilities));
        double entropyY = calculateEntropy(getMarginalProbabilitiesY(probabilities));
        double conditionalEntropyXY = calculateConditionalEntropy(probabilities);
        double conditionalEntropyYX = calculateConditionalEntropy(transposeMatrix(probabilities));
        double jointEntropy = calculateEntropy(flattenMatrix(probabilities));
        double mutualInformation = entropyX + entropyY - jointEntropy;
        double divergencePXtoPY = calculateDivergence(probabilities, getMarginalProbabilitiesY(probabilities));
        double divergencePYtoPX = calculateDivergence(transposeMatrix(probabilities), getMarginalProbabilitiesX(probabilities));

        System.out.println("H(X): " + entropyX);
        System.out.println("H(Y): " + entropyY);
        System.out.println("H(X | Y): " + conditionalEntropyXY);
        System.out.println("H(Y | X): " + conditionalEntropyYX);
        System.out.println("H(X, Y): " + jointEntropy);
        System.out.println("H(Y) - H(Y | X): " + (entropyY - conditionalEntropyYX));
        System.out.println("I(X; Y): " + mutualInformation);
        System.out.println("D(P(x) || P(y)): " + divergencePXtoPY);
        System.out.println("D(P(y) || P(x)): " + divergencePYtoPX);
    }

    // Tính entropy
    public static double calculateEntropy(double[] probabilities) {
        double entropy = 0.0;
        for (double p : probabilities) {
            if (p > 0) {
                entropy -= p * Math.log(p) / Math.log(2);
            }
        }
        return entropy;
    }

    // Tính xác suất biên P(X)
    public static double[] getMarginalProbabilitiesX(double[][] probabilities) {
        int M = probabilities.length;
        int N = probabilities[0].length;
        double[] marginalProbabilitiesX = new double[M];
        for (int i = 0; i < M; i++) {
            double sum = 0.0;
            for (int j = 0; j < N; j++) {
                sum += probabilities[i][j];
            }
            marginalProbabilitiesX[i] = sum;
        }
        return marginalProbabilitiesX;
    }

    // Tính xác suất biên P(Y)
    public static double[] getMarginalProbabilitiesY(double[][] probabilities) {
        int M = probabilities.length;
        int N = probabilities[0].length;
        double[] marginalProbabilitiesY = new double[N];
        for (int j = 0; j < N; j++) {
            double sum = 0.0;
            for (int i = 0; i < M; i++) {
                sum += probabilities[i][j];
            }
            marginalProbabilitiesY[j] = sum;
        }
        return marginalProbabilitiesY;
    }

    // Tính entropy có điều kiện H(X | Y) hoặc H(Y | X)
    public static double calculateConditionalEntropy(double[][] probabilities) {
        double conditionalEntropy = 0.0;
        int M = probabilities.length;
        int N = probabilities[0].length;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double pxy = probabilities[i][j];
                double py = getMarginalProbabilitiesY(probabilities)[j];
                if (pxy > 0 && py > 0) {
                    conditionalEntropy -= pxy * Math.log(pxy / py) / Math.log(2);
                }
            }
        }

        return conditionalEntropy;
    }

    // Chuyển vị ma trận
    public static double[][] transposeMatrix(double[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;
        double[][] transpose = new double[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                transpose[i][j] = matrix[j][i];
            }
        }
        return transpose;
    }

    // Biến ma trận 2D thành mảng 1D
    public static double[] flattenMatrix(double[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;
        double[] flattened = new double[M * N];
        int index = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                flattened[index++] = matrix[i][j];
            }
        }
        return flattened;
    }

    // Tính độ lệch KL D(P(x) || P(y)) hoặc D(P(y) || P(x))
    public static double calculateDivergence(double[][] source, double[] target) {
        double divergence = 0.0;
        int M = source.length;
        int N = source[0].length;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double pxy = source[i][j];
                double py = target[j];
                if (pxy > 0 && py > 0) {
                    divergence += pxy * Math.log(pxy / py) / Math.log(2);
                }
            }
        }

        return divergence;
    }
}
