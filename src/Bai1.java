import java.util.Scanner;

public class Bai1 {
    //Cau a
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Moi nhap ma tran[M*N]:");
        System.out.print("Moi nhap M:");
        // nhap so M
        String M = sc.nextLine();
        int m = Integer.parseInt(M);
        //nhap so N
        System.out.print("Moi nhap  N:");
        String N = sc.nextLine();
        int n = Integer.parseInt(N);
        double I[][] = new double[m][n];
        // nhap phan tu cho ma tran

        //nhap so lieu cho ma tran
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                while (true) {
                    System.out.print("Moi nhap: I[" + i + "," + j + "]:");
                    String a = sc.nextLine();
                    I[i][j] = Double.parseDouble(a);
                    if (I[i][j] < 0) {
                        System.out.println("Nhap sai moi nhap lai");
                    } else {
                        break;
                    }
                }
            }
        }
        //Tính và hiển thị H(X), H(Y),H(X|Y), H(Y|X),H(X,Y),H(Y) − H(Y|X),I(X;Y)
        double entropyX = tinhEntropy(TinhBienPx(I));
        double entropyY = tinhEntropy(TinhBienPy(I));
        double entropyHXY = tinhEntropyCoDieuKienXY(I);
        double entropyHYX = tinhEntropyCoDieuKienYX(I);
        double jointentropy = tinhJointEntropy(I);
        double HYtruHYX = entropyY - entropyHYX;
        double IXY = entropyX + entropyY - jointentropy;
        System.out.println("H(X): " + entropyX);
        System.out.println("H(Y): " + entropyY);
        System.out.println("H(X|Y): " + entropyHXY);
        System.out.println("H(Y|X): " + entropyHYX);
        System.out.println("H(X,Y): " + jointentropy);
        System.out.println("H(Y) - H(Y-X)" + HYtruHYX);
        System.out.println("I(X,Y): " + IXY);
        //Tính D(P(x)||P(y)) và D(P(y)||P(x)

    }

    //tinh bien P(x)
    public static double[] TinhBienPx(double[][] I) {

        int M = I.length;
        int N = I[0].length;
        double[] BienPX = new double[M];
        for (int i = 0; i < M; i++) {
            double sum = 0.0;
            for (int j = 0; j < N; j++) {
                sum += I[j][i];
            }
            BienPX[i] = sum;
        }
        return BienPX;
    }

    //Tinh bien P(y)
    public static double[] TinhBienPy(double[][] I) {
        int M = I.length;
        int N = I[0].length;
        double BienPY[] = new double[M];
        for (int i = 0; i < M; i++) {
            double sum = 0.0;
            for (int j = 0; j < N; j++) {
                sum += I[i][j];
            }
            BienPY[i] = sum;
        }
        return BienPY;
    }

    // Tinh entropy H(x|y)
    public static double tinhEntropyCoDieuKienXY(double[][] I) {
        int M = I.length;
        int N = I[0].length;
        double entropycoDK = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double pxy = I[i][j];
                double py = TinhBienPy(I)[j];
                if (pxy > 0 && py > 0) {
                    entropycoDK -= pxy * Math.log(pxy / py) / Math.log(2);
                }
            }
        }
        return entropycoDK;
    }

    // Tinh entropy H(y|x)
    public static double tinhEntropyCoDieuKienYX(double[][] I) {
        int M = I.length;
        int N = I[0].length;
        double entropycoDK = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double pxy = I[i][j];
                double px = TinhBienPx(I)[j];
                if (pxy > 0 && px > 0) {
                    entropycoDK -= pxy * Math.log(pxy / px) / Math.log(2);
                }
            }
        }
        return entropycoDK;
    }

    public static double tinhJointEntropy(double[][] I) {
        int M = I.length;
        int N = I[0].length;
        double jointentropy = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                jointentropy -= I[i][j];
            }

        }
        return jointentropy;
    }

    public static double tinhEntropy(double[] p) {
        double entropy = 0.0;
        for (double i : p
        ) {
            if (i > 0) {
                entropy -= i * log2(i);
            }
        }
        return entropy;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}


