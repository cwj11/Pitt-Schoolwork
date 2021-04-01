import java.io.FileNotFoundException;

public class vmsimTester {
  public static void main(String[] args) throws FileNotFoundException {
    int[] pageSizes = {4, 8, 16, 32};
    int[] numFrames = {8, 16, 32, 64};
    String[] traceFiles = {"gcc.trace", "go.trace", "hmmer.trace", "mcf.trace", "art.trace"};

    for (int i = 0; i < pageSizes.length; i++) {
      for (int j = 0; j < numFrames.length; j++) {
        for (int k = 0; k < traceFiles.length; k++) {
          String[] argsOPT = {"-n", Integer.toString(numFrames[j]), "-p", Integer.toString(pageSizes[i]), "-a", "opt", traceFiles[k]};
          String[] argsLRU = {"-n", Integer.toString(numFrames[j]), "-p", Integer.toString(pageSizes[i]), "-a", "lru", traceFiles[k]};
          System.out.println(traceFiles[k]);
          vmsim.main(argsOPT);
          vmsim.main(argsLRU);
          System.out.println("***************************************************");
        }
      }
    }
  }
}
